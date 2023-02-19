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
    indicatorCodeAI = 'PRPI1_01'
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

    print("indicatorIdsOsmosys: " + str(indicatorIdsOsmosys))
    indicatorIdsOsmosysTranferencias = np.array([201])
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

        poblacion_meta = ['Refugiados/as y migrantes', 'Comunidad de acogida']

        PRPI1_01_RM_NA = int(df.loc[df['age_gender'] == 'NINAS'].iloc[0].value_a)
        PRPI1_01_RM_NN = int(df.loc[df['age_gender'] == 'NINOS'].iloc[0].value_a)
        PRPI1_01_RM_OTR = int(df.loc[df['age_gender'] == 'OTRO'].iloc[0].value_a)
        dfDiversidad = osmosys.osmosys.getRefLgbtiDiscapacitadosValues(year=year, month=month, orgOsmosys=orgAcron,
                                                                       indicatorsIdsOmosys=indicatorIdsOsmosysServicios,
                                                                       cantonCode=cantonCode)
        PRPI1_01_RM_LGBT = int(dfDiversidad.loc[(dfDiversidad['diversity_type'] == 'LGBTI') & (
                dfDiversidad['country_of_origin'] == 'VENEZUELA')].iloc[0].value_a)
        PRPI1_01_RM_DS = int(dfDiversidad.loc[(dfDiversidad['diversity_type'] == 'DISCAPACITADOS') & (
                dfDiversidad['country_of_origin'] == 'VENEZUELA')].iloc[0].value_a)

        dfCa = osmosys.osmosys.getCAValues(year=year, month=month, orgOsmosys=orgAcron,
                                           indicatorsIdsOmosys=indicatorIdsOsmosysServicios, cantonCode=cantonCode)
        PRPI1_01_CA_NA = int(dfCa.loc[df['age_gender'] == 'NINAS'].iloc[0].value_a)
        PRPI1_01_CA_NN = int(dfCa.loc[df['age_gender'] == 'NINOS'].iloc[0].value_a)
        PRPI1_01_CA_OTR = int(dfCa.loc[df['age_gender'] == 'OTRO'].iloc[0].value_a)

        PRPI1_01_CA_LGBT = int(dfDiversidad.loc[(dfDiversidad['diversity_type'] == 'LGBTI') & (
                dfDiversidad['country_of_origin'] == 'ECUADOR')].iloc[0].value_a)
        PRPI1_01_CA_DS = int(dfDiversidad.loc[(dfDiversidad['diversity_type'] == 'DISCAPACITADOS') & (
                dfDiversidad['country_of_origin'] == 'ECUADOR')].iloc[0].value_a)
        PRPI1_01_CUAL = osmosys.osmosys.getCommentary(year=year, month=month, orgOsmosys=orgAcron,
                                                      indicatorsIdsOmosys=indicatorIdsOsmosysServicios).iloc[
            0].value_a

        subform = model.modelAI.SubFormPRPI1_01(
            mes=month_number,
            colltmgkykvhxgij6=indicatorIdAI,
            rmrp='Si',
            poblacion_meta=poblacion_meta,
            modalidad_impl='cmo12q1kykwwosjka',  ## servicio
            mecanismos=None,
            transferencia=None,
            usd_transfer=None,
            PRPI1_01_RM_NA=PRPI1_01_RM_NA,
            PRPI1_01_RM_NN=PRPI1_01_RM_NN,
            PRPI1_01_RM_OTR=PRPI1_01_RM_OTR,
            PRPI1_01_RM_LGBT=PRPI1_01_RM_LGBT,
            PRPI1_01_RM_DS=PRPI1_01_RM_DS,
            PRPI1_01_CA_NA=PRPI1_01_CA_NA,
            PRPI1_01_CA_NN=PRPI1_01_CA_NN,
            PRPI1_01_CA_OTR=PRPI1_01_CA_OTR,
            PRPI1_01_CA_LGBT=PRPI1_01_CA_LGBT,
            PRPI1_01_CA_DS=PRPI1_01_CA_DS,
            PRPI1_01_CUAL=PRPI1_01_CUAL
        )
        newId = generate_id()
        newIds.append(newId)
        record = model.modelAI.Record(formId=formIdAI, recordId=newId, parentRecordId=row['@id'], fields=subform)
        changesList.append(record)
    ## los que  son transferencias
    for index, row in parentFormsIdsTransferencias.iterrows():

        orgAcron = row.acronym

        cantonCode = row.canton_code
        df = osmosys.osmosys.getRefValues(year=year, month=month, orgOsmosys=orgAcron,
                                          indicatorsIdsOmosys=indicatorIdsOsmosysTranferencias, cantonCode=cantonCode)
        if df.loc[df['age_gender'] == 'NINAS'].iloc[0].value_a is None:
            print('No value')
            continue

        poblacion_meta = ['Refugiados/as y migrantes', 'Comunidad de acogida']
        PRPI1_01_RM_NA = int(df.loc[df['age_gender'] == 'NINAS'].iloc[0].value_a)
        PRPI1_01_RM_NN = int(df.loc[df['age_gender'] == 'NINOS'].iloc[0].value_a)
        PRPI1_01_RM_OTR = int(df.loc[df['age_gender'] == 'OTRO'].iloc[0].value_a)
        dfDiversidad = osmosys.osmosys.getRefLgbtiDiscapacitadosValues(year=year, month=month, orgOsmosys=orgAcron,
                                                                       indicatorsIdsOmosys=indicatorIdsOsmosysTranferencias,
                                                                       cantonCode=cantonCode)
        PRPI1_01_RM_LGBT = int(dfDiversidad.loc[(dfDiversidad['diversity_type'] == 'LGBTI') & (
                dfDiversidad['country_of_origin'] == 'VENEZUELA')].iloc[0].value_a)
        PRPI1_01_RM_DS = int(dfDiversidad.loc[(dfDiversidad['diversity_type'] == 'DISCAPACITADOS') & (
                dfDiversidad['country_of_origin'] == 'VENEZUELA')].iloc[0].value_a)

        dfCa = osmosys.osmosys.getCAValues(year=year, month=month, orgOsmosys=orgAcron,
                                           indicatorsIdsOmosys=indicatorIdsOsmosysTranferencias, cantonCode=cantonCode)
        PRPI1_01_CA_NA = int(dfCa.loc[df['age_gender'] == 'NINAS'].iloc[0].value_a)
        PRPI1_01_CA_NN = int(dfCa.loc[df['age_gender'] == 'NINOS'].iloc[0].value_a)
        PRPI1_01_CA_OTR = int(dfCa.loc[df['age_gender'] == 'OTRO'].iloc[0].value_a)

        PRPI1_01_CA_LGBT = int(dfDiversidad.loc[(dfDiversidad['diversity_type'] == 'LGBTI') & (
                dfDiversidad['country_of_origin'] == 'ECUADOR')].iloc[0].value_a)
        PRPI1_01_CA_DS = int(dfDiversidad.loc[(dfDiversidad['diversity_type'] == 'DISCAPACITADOS') & (
                dfDiversidad['country_of_origin'] == 'ECUADOR')].iloc[0].value_a)
        PRPI1_01_CUAL = commentary = osmosys.osmosys.getCommentary(year=year, month=month, orgOsmosys=orgAcron,
                                                                   indicatorsIdsOmosys=indicatorIdsOsmosysTranferencias).iloc[
            0].value_a
        usd_transfer = int(osmosys.osmosys.getCBIBudget(year=year, month=month, orgOsmosys=orgAcron,
                                                        indicatorsIdsOmosys=indicatorIdsOsmosysTranferencias,
                                                        cantonCode=cantonCode).iloc[0].budget)
        subform = model.modelAI.SubFormPRPI1_01(
            mes=month_number,
            colltmgkykvhxgij6=indicatorIdAI,
            rmrp='Si',
            poblacion_meta=poblacion_meta,
            modalidad_impl='c3jf6gykykwwemuk7',  ## efectivo
            mecanismos='c3de3pmkym0dbino',  ## TM CAJERO
            transferencia='Incondicional',
            usd_transfer=usd_transfer,
            PRPI1_01_RM_NA=PRPI1_01_RM_NA,
            PRPI1_01_RM_NN=PRPI1_01_RM_NN,
            PRPI1_01_RM_OTR=PRPI1_01_RM_OTR,
            PRPI1_01_RM_LGBT=PRPI1_01_RM_LGBT,
            PRPI1_01_RM_DS=PRPI1_01_RM_DS,
            PRPI1_01_CA_NA=PRPI1_01_CA_NA,
            PRPI1_01_CA_NN=PRPI1_01_CA_NN,
            PRPI1_01_CA_OTR=PRPI1_01_CA_OTR,
            PRPI1_01_CA_LGBT=PRPI1_01_CA_LGBT,
            PRPI1_01_CA_DS=PRPI1_01_CA_DS,
            PRPI1_01_CUAL=PRPI1_01_CUAL
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
