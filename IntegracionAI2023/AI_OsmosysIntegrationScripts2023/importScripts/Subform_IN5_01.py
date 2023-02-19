#%%
import pandas as pd

import osmosys.osmosys
import osmosys.Backups
import activityinfo
from activityinfo import Client
from activityinfo import FormProcessing
import importlib
import model.modelAI
from activityinfo.id import generate_id
import json
from requests.exceptions import HTTPError
import os.path
import numpy as np

def importForm(month, month_number, year, test):
    ## parameters
    indicatorCodeAI = 'IN5_01'
    print(
        '--------------------------------------' + indicatorCodeAI + '------------------------------------------------------')
    # busco matchs
    subformDf = osmosys.osmosys.getMatchSubforms(indicatorCodeAI)
    ## print(subformDf.info)
    indicatorIdsOsmosys = subformDf.osmosys_indicator_id.unique()
    subformSeries = subformDf.iloc[0]
    formIdAI = subformSeries.form_id
    parentFormIdAI = subformSeries.parent_form_id
    indicatorIdAI = subformSeries.indicador_ai_id
    print("formIdAI: " + formIdAI)
    print("parentFormIdAI: " + parentFormIdAI)
    print("indicatorIdAI: " + indicatorIdAI)
    print("indicatorIdsOsmosys: " + str(indicatorIdsOsmosys))

    ## consulto los datos reportados
    print("indicatorIdsOsmosys: " + str(indicatorIdsOsmosys))
    indicatorIdsOsmosysTranferencias = np.array([273, 287, 280])
    print("indicatorIdsOsmosysTranferencias: " + str(indicatorIdsOsmosysTranferencias))
    indicatorIdsOsmosysServicios = np.setdiff1d(indicatorIdsOsmosys, indicatorIdsOsmosysTranferencias)
    print("indicatorIdsOsmosysServicios: " + str(indicatorIdsOsmosysServicios))

    reportedDfServicios = osmosys.osmosys.getIesPartnerCantonsByIndicatorsIdsOsmosysAndMonth(
        indicatorsIdsOmosys=indicatorIdsOsmosysServicios,
        month=month, year=year)
    reportedDfTransferencias = osmosys.osmosys.getIesPartnerCantonsByIndicatorsIdsOsmosysAndMonth(
        indicatorsIdsOmosys=indicatorIdsOsmosysTranferencias,
        month=month, year=year)
    print('reported data Servicios: ' + str(reportedDfServicios.shape[0]))
    print('reported data transferencias: ' + str(reportedDfTransferencias.shape[0]))

    ## obtengo los parents de AI
    parentFormsIdsServicios = FormProcessing.getParentsIds(parentFormIdAI=parentFormIdAI,
                                                           reportedDf=reportedDfServicios)
    parentFormsIdsTransferencias = FormProcessing.getParentsIds(parentFormIdAI=parentFormIdAI,
                                                                reportedDf=reportedDfTransferencias)

    ## contruyo las estructuras de datos
    changesList = []
    newIds = []
    ## los que  son servicios
    for index, row in parentFormsIdsServicios.iterrows():
        # print(row)
        orgAcron = row.acronym
        cantonCode = row.canton_code
        df = osmosys.osmosys.getRefValues(year=year, month=month, orgOsmosys=orgAcron,
                                          indicatorsIdsOmosys=indicatorIdsOsmosysServicios, cantonCode=cantonCode)

        poblacion_meta = ["Refugiados/as y migrantes", "Comunidad de acogida"]
        IN5_01_RM_MM_N = int(df.loc[df['age_gender'] == 'ADULTAS'].iloc[0].value_a)
        IN5_01_RM_HH_N = int(df.loc[df['age_gender'] == 'ADULTOS'].iloc[0].value_a)
        IN5_01_RM_OTR_N = int(df.loc[df['age_gender'] == 'OTRO'].iloc[0].value_a)

        dfCa = osmosys.osmosys.getCAValues(year=year, month=month, orgOsmosys=orgAcron,
                                           indicatorsIdsOmosys=indicatorIdsOsmosysServicios, cantonCode=cantonCode)
        IN5_01_CA_MM_N = int(dfCa.loc[df['age_gender'] == 'ADULTAS'].iloc[0].value_a)
        IN5_01_CA_HH_N = int(dfCa.loc[df['age_gender'] == 'ADULTOS'].iloc[0].value_a)
        IN5_01_CA_OTR_N = int(dfCa.loc[df['age_gender'] == 'OTRO'].iloc[0].value_a)

        commentary = osmosys.osmosys.getCommentary(year=year, month=month, orgOsmosys=orgAcron,
                                                   indicatorsIdsOmosys=indicatorIdsOsmosysServicios).iloc[
            0].value_a

        dfDiversidad = osmosys.osmosys.getRefLgbtiDiscapacitadosValues(year=year, month=month, orgOsmosys=orgAcron,
                                                                       indicatorsIdsOmosys=indicatorIdsOsmosysServicios,
                                                                       cantonCode=cantonCode)
        IN5_01_RM_LGBT_N = int(dfDiversidad.loc[(dfDiversidad['diversity_type'] == 'LGBTI') & (
                dfDiversidad['country_of_origin'] == 'VENEZUELA')].iloc[0].value_a)

        IN5_01_CA_LGBT_N = int(dfDiversidad.loc[(dfDiversidad['diversity_type'] == 'LGBTI') & (
                dfDiversidad['country_of_origin'] == 'ECUADOR')].iloc[0].value_a)

        subform = model.modelAI.SubFormIN5_01(
            mes=month_number,
            colltmgkykvhxgij6=indicatorIdAI,
            rmrp='Si',
            poblacion_meta=poblacion_meta,
            poblacion_meta_freq='Primera vez',
            modalidad_impl='cmo12q1kykwwosjka',  ##'Provisión de servicio',
            mecanismos=None,
            transferencia=None,
            usd_transfer=None,
            semilla='Si',
            IN5_01_RM_MM_N=IN5_01_RM_MM_N,
            IN5_01_RM_HH_N=IN5_01_RM_HH_N,
            IN5_01_RM_OTR_N=IN5_01_RM_OTR_N,
            IN5_01_RM_LGBT_N=IN5_01_RM_LGBT_N,
            IN5_01_CA_MM_N=IN5_01_CA_MM_N,
            IN5_01_CA_HH_N=IN5_01_CA_HH_N,
            IN5_01_CA_OTR_N=IN5_01_CA_OTR_N,
            IN5_01_CA_LGBT_N=IN5_01_CA_LGBT_N,
            IN5_01_CUAL=commentary
        )
        newId = generate_id()
        newIds.append(newId)
        record = model.modelAI.Record(formId=formIdAI, recordId=newId, parentRecordId=row['@id'], fields=subform)
        changesList.append(record)
    ## Los que son transferencias
    for index, row in parentFormsIdsTransferencias.iterrows():
        # print(row)
        orgAcron = row.acronym
        cantonCode = row.canton_code
        df = osmosys.osmosys.getRefValues(year=year, month=month, orgOsmosys=orgAcron,
                                          indicatorsIdsOmosys=indicatorIdsOsmosysTranferencias, cantonCode=cantonCode)

        poblacion_meta = ["Refugiados/as y migrantes", "Comunidad de acogida"]
        IN5_01_RM_MM_N = int(df.loc[df['age_gender'] == 'ADULTAS'].iloc[0].value_a)
        IN5_01_RM_HH_N = int(df.loc[df['age_gender'] == 'ADULTOS'].iloc[0].value_a)
        IN5_01_RM_OTR_N = int(df.loc[df['age_gender'] == 'OTRO'].iloc[0].value_a)

        dfCa = osmosys.osmosys.getCAValues(year=year, month=month, orgOsmosys=orgAcron,
                                           indicatorsIdsOmosys=indicatorIdsOsmosysTranferencias, cantonCode=cantonCode)
        IN5_01_CA_MM_N = int(dfCa.loc[df['age_gender'] == 'ADULTAS'].iloc[0].value_a)
        IN5_01_CA_HH_N = int(dfCa.loc[df['age_gender'] == 'ADULTOS'].iloc[0].value_a)
        IN5_01_CA_OTR_N = int(dfCa.loc[df['age_gender'] == 'OTRO'].iloc[0].value_a)

        commentary = osmosys.osmosys.getCommentary(year=year, month=month, orgOsmosys=orgAcron,
                                                   indicatorsIdsOmosys=indicatorIdsOsmosysTranferencias).iloc[
            0].value_a

        dfDiversidad = osmosys.osmosys.getRefLgbtiDiscapacitadosValues(year=year, month=month, orgOsmosys=orgAcron,
                                                                       indicatorsIdsOmosys=indicatorIdsOsmosysTranferencias,
                                                                       cantonCode=cantonCode)
        IN5_01_RM_LGBT_N = int(dfDiversidad.loc[(dfDiversidad['diversity_type'] == 'LGBTI') & (
                dfDiversidad['country_of_origin'] == 'VENEZUELA')].iloc[0].value_a)

        IN5_01_CA_LGBT_N = int(dfDiversidad.loc[(dfDiversidad['diversity_type'] == 'LGBTI') & (
                dfDiversidad['country_of_origin'] == 'ECUADOR')].iloc[0].value_a)

        usd_transfer = int(osmosys.osmosys.getCBIBudget(year=year, month=month, orgOsmosys=orgAcron,
                                                        indicatorsIdsOmosys=indicatorIdsOsmosysTranferencias,
                                                        cantonCode=cantonCode).iloc[0].budget)

        subform = model.modelAI.SubFormIN5_01(
            mes=month_number,
            colltmgkykvhxgij6=indicatorIdAI,
            rmrp='Si',
            poblacion_meta=poblacion_meta,
            poblacion_meta_freq='Primera vez',
            modalidad_impl='c3jf6gykykwwemuk7',  ##'Efectivo',
            mecanismos='c3de3pmkym0dbino',  ## Entrega en ATM/Cajero automático
            transferencia='Incondicional',
            usd_transfer=usd_transfer,
            semilla='Si',
            IN5_01_RM_MM_N=IN5_01_RM_MM_N,
            IN5_01_RM_HH_N=IN5_01_RM_HH_N,
            IN5_01_RM_OTR_N=IN5_01_RM_OTR_N,
            IN5_01_RM_LGBT_N=IN5_01_RM_LGBT_N,
            IN5_01_CA_MM_N=IN5_01_CA_MM_N,
            IN5_01_CA_HH_N=IN5_01_CA_HH_N,
            IN5_01_CA_OTR_N=IN5_01_CA_OTR_N,
            IN5_01_CA_LGBT_N=IN5_01_CA_LGBT_N,
            IN5_01_CUAL=commentary
        )
        newId = generate_id()
        newIds.append(newId)
        record = model.modelAI.Record(formId=formIdAI, recordId=newId, parentRecordId=row['@id'], fields=subform)
        changesList.append(record)
    print('changes to charge: ' + str(len(changesList)))

    changes = model.modelAI.Changes(changesList)
    finalJson = json.dumps(changes, default=model.modelAI.default)

    ## creo respaldo
    osmosys.Backups.do_backup(indicatorCodeAI=indicatorCodeAI, indicatorIdsOsmosys=indicatorIdsOsmosys, month=month,
                              year=year, changesList=changesList, finalJson=finalJson)

    ## envio a AI
    if (test):
        print(
            '--------------------------------------' + indicatorCodeAI + '-' + str(
                indicatorIdsOsmosys) + '------------------------------------------------------')
    else:
        try:
            if len(changesList) > 0:
                print('se envia a AI')
                client = Client(token=osmosys.osmosys.getToken(), base_url='https://www.activityinfo.org/resources')
                client.post_resource(path='update', body=finalJson)
                print(
                    '--------------------------------------' + indicatorCodeAI + '-' + str(
                        indicatorIdsOsmosys) + '------------------------------------------------------')
            else:
                print('nada que enviar a AI')
        except HTTPError as e:
            code = e.response.status_code
            print('error:')
            print(code)

            print(e)
    print(
        '-------------------------------------- FIN ' + indicatorCodeAI + '------------------------------------------------------')
