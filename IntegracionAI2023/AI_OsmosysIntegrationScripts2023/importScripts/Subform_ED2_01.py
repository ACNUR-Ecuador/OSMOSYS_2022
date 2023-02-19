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

def importForm(month, month_number, year, test):
    ## parameters
    indicatorCodeAI = 'ED2_01'
    print(
        '--------------------------------------' + indicatorCodeAI + '------------------------------------------------------')
    # busco matchs
    subformDf = osmosys.osmosys.getMatchSubforms(indicatorCodeAI)
    print(subformDf.columns)
    indicatorIdsOsmosys = subformDf.osmosys_indicator_id.unique()
    subformSeries = subformDf.iloc[0]
    formIdAI = subformSeries.form_id
    parentFormIdAI = subformSeries.parent_form_id
    indicatorIdAI = subformSeries.indicador_ai_id
    osmosys_indicator_code = subformDf.osmosys_indicator_code.unique()
    print("formIdAI: " + formIdAI)
    print("parentFormIdAI: " + parentFormIdAI)
    print("indicatorIdAI: " + indicatorIdAI)
    print("indicatorIdsOsmosys: " + str(indicatorIdsOsmosys))
    print("osmosys_indicator_code: " + str(osmosys_indicator_code))

    print("indicatorIdsOsmosys: " + str(indicatorIdsOsmosys))
    ## consulto los datos reportados, este indicador tiene 2 diferentes modos de servicio, por lo que tendra diferentes condiciones
    ## 210 10L02	No. de niños, niñas y adolescentes que reciben acompañamiento individual y acceden a la educación
    ## 217 10L09	No. de personas por las cuales ACNUR trabaja que reciben transferencias monetarias
    indicatorIdsOsmosys_10L02 = [210]
    indicatorIdsOsmosys_10L09 = [217]
    reportedDf_10L02 = osmosys.osmosys.getIesPartnerCantonsByIndicatorsIdsOsmosysAndMonth(
        indicatorsIdsOmosys=indicatorIdsOsmosys_10L02,
        month=month, year=year)
    reportedDf_10L09 = osmosys.osmosys.getIesPartnerCantonsByIndicatorsIdsOsmosysAndMonth(
        indicatorsIdsOmosys=indicatorIdsOsmosys_10L09,
        month=month, year=year)
    print('reported data: ' + str(indicatorIdsOsmosys_10L02) + ': ' + str(reportedDf_10L02.shape[0]))
    print('reported data: ' + str(indicatorIdsOsmosys_10L09) + ': ' + str(reportedDf_10L09.shape[0]))

    ## obtengo los parents de AI
    parentFormsIds_10L02 = FormProcessing.getParentsIds(parentFormIdAI=parentFormIdAI, reportedDf=reportedDf_10L02)
    parentFormsIds_10L09 = FormProcessing.getParentsIds(parentFormIdAI=parentFormIdAI, reportedDf=reportedDf_10L09)

    ## contruyo las estructuras de datos

    changesList = []
    newIds = []

    ## 210 10L02	No. de niños, niñas y adolescentes que reciben acompañamiento individual y acceden a la educación
    for index, row in parentFormsIds_10L02.iterrows():
        # print(row)
        orgAcron = row.acronym
        cantonCode = row.canton_code
        df = osmosys.osmosys.getRefValues(year=year, month=month, orgOsmosys=orgAcron,
                                          indicatorsIdsOmosys=indicatorIdsOsmosys_10L02, cantonCode=cantonCode)

        poblacion_meta = ["Refugiados/as y migrantes", "Comunidad de acogida"]

        ED2_01_RM_NA = int(df.loc[df['age_gender'] == 'NINAS'].iloc[0].value_a)
        ED2_01_RM_NN = int(df.loc[df['age_gender'] == 'NINOS'].iloc[0].value_a)
        ED2_01_RM_OTR = 0
        ED2_01_RM_ADM = 0
        ED2_01_RM_ADH = 0
        ED2_01_RM_ADOTR = 0
        ED2_01_RM_MM = int(df.loc[df['age_gender'] == 'ADULTAS'].iloc[0].value_a)
        ED2_01_RM_HH = int(df.loc[df['age_gender'] == 'ADULTOS'].iloc[0].value_a)

        dfCa = osmosys.osmosys.getCAValues(year=year, month=month, orgOsmosys=orgAcron,
                                           indicatorsIdsOmosys=indicatorIdsOsmosys_10L02, cantonCode=cantonCode)

        ED2_01_CA_NA = int(dfCa.loc[df['age_gender'] == 'NINAS'].iloc[0].value_a)
        ED2_01_CA_NN = int(dfCa.loc[df['age_gender'] == 'NINOS'].iloc[0].value_a)
        ED2_01_CA_OTR = 0
        ED2_01_CA_ADM = 0
        ED2_01_CA_ADH = 0
        ED2_01_CA_ADOTR = 0
        ED2_01_CA_MM = int(dfCa.loc[df['age_gender'] == 'ADULTAS'].iloc[0].value_a)
        ED2_01_CA_HH = int(dfCa.loc[df['age_gender'] == 'ADULTOS'].iloc[0].value_a)

        ED2_01_RM_DS = 0

        ED2_01_CA_DS = 0
        commentary = osmosys.osmosys.getCommentary(year=year, month=month, orgOsmosys=orgAcron,
                                                   indicatorsIdsOmosys=indicatorIdsOsmosys_10L02).iloc[
            0].value_a

        subform = model.modelAI.SubFormED2_01(
            mes=month_number,
            colltmgkykvhxgij6=indicatorIdAI,
            rmrp='Si',
            poblacion_meta=poblacion_meta,
            modalidad_impl='cmo12q1kykwwosjka',  ##'Provisión de servicio',
            mecanismos=None,
            transferencia=None,
            usd_transfer=None,
            ED2_01_RM_NA=ED2_01_RM_NA,
            ED2_01_RM_NN=ED2_01_RM_NN,
            ED2_01_RM_OTR=ED2_01_RM_OTR,
            ED2_01_RM_ADM=ED2_01_RM_ADM,
            ED2_01_RM_ADH=ED2_01_RM_ADH,
            ED2_01_RM_ADOTR=ED2_01_RM_ADOTR,
            ED2_01_RM_MM=ED2_01_RM_MM,
            ED2_01_RM_HH=ED2_01_RM_HH,
            ED2_01_RM_DS=ED2_01_RM_DS,
            ED2_01_CA_NA=ED2_01_CA_NA,
            ED2_01_CA_NN=ED2_01_CA_NN,
            ED2_01_CA_OTR=ED2_01_CA_OTR,
            ED2_01_CA_ADM=ED2_01_CA_ADM,
            ED2_01_CA_ADH=ED2_01_CA_ADH,
            ED2_01_CA_ADOTR=ED2_01_CA_ADOTR,
            ED2_01_CA_MM=ED2_01_CA_MM,
            ED2_01_CA_HH=ED2_01_CA_HH,
            ED2_01_CA_DS=ED2_01_CA_DS,
            ED2_01_CUAL=commentary,

        )
        newId = generate_id()
        newIds.append(newId)
        record = model.modelAI.Record(formId=formIdAI, recordId=newId, parentRecordId=row['@id'], fields=subform)
        changesList.append(record)
    ## 217	10L09	No. de personas por las cuales ACNUR trabaja que reciben transferencias monetarias	Educación
    for index, row in parentFormsIds_10L09.iterrows():
        # print(row)
        orgAcron = row.acronym
        cantonCode = row.canton_code
        df = osmosys.osmosys.getRefValues(year=year, month=month, orgOsmosys=orgAcron,
                                          indicatorsIdsOmosys=indicatorIdsOsmosys_10L09, cantonCode=cantonCode)

        poblacion_meta = ["Refugiados/as y migrantes", "Comunidad de acogida"]

        print(df.size)
        ED2_01_RM_NA = int(df.loc[df['age_gender'] == 'NINAS'].iloc[0].value_a)
        ED2_01_RM_NN = int(df.loc[df['age_gender'] == 'NINOS'].iloc[0].value_a)
        ED2_01_RM_OTR = 0
        ED2_01_RM_ADM = 0
        ED2_01_RM_ADH = 0
        ED2_01_RM_ADOTR = 0
        ED2_01_RM_MM = int(df.loc[df['age_gender'] == 'ADULTAS'].iloc[0].value_a)
        ED2_01_RM_HH = int(df.loc[df['age_gender'] == 'ADULTOS'].iloc[0].value_a)

        dfCa = osmosys.osmosys.getCAValues(year=year, month=month, orgOsmosys=orgAcron,
                                           indicatorsIdsOmosys=indicatorIdsOsmosys_10L09, cantonCode=cantonCode)

        ED2_01_CA_NA = int(dfCa.loc[df['age_gender'] == 'NINAS'].iloc[0].value_a)
        ED2_01_CA_NN = int(dfCa.loc[df['age_gender'] == 'NINOS'].iloc[0].value_a)
        ED2_01_CA_OTR = 0
        ED2_01_CA_ADM = 0
        ED2_01_CA_ADH = 0
        ED2_01_CA_ADOTR = 0
        ED2_01_CA_MM = int(dfCa.loc[df['age_gender'] == 'ADULTAS'].iloc[0].value_a)
        ED2_01_CA_HH = int(dfCa.loc[df['age_gender'] == 'ADULTOS'].iloc[0].value_a)

        ED2_01_RM_DS = 0

        ED2_01_CA_DS = 0
        commentary = osmosys.osmosys.getCommentary(year=year, month=month, orgOsmosys=orgAcron,
                                                   indicatorsIdsOmosys=indicatorIdsOsmosys_10L09).iloc[
            0].value_a
        usd_transfer = int(osmosys.osmosys.getCBIBudget(year=year, month=month, orgOsmosys=orgAcron,
                                                        indicatorsIdsOmosys=indicatorIdsOsmosys,
                                                        cantonCode=cantonCode).iloc[0].budget)
        subform = model.modelAI.SubFormED2_01(
            mes=month_number,
            colltmgkykvhxgij6=indicatorIdAI,
            rmrp='Si',
            poblacion_meta=poblacion_meta,
            modalidad_impl='c3jf6gykykwwemuk7',  ## 'Efectivo',
            mecanismos='c3de3pmkym0dbino',  ##'Entrega en ATM/Cajero automático',
            transferencia='Incondicional',
            usd_transfer=usd_transfer,
            ED2_01_RM_NA=ED2_01_RM_NA,
            ED2_01_RM_NN=ED2_01_RM_NN,
            ED2_01_RM_OTR=ED2_01_RM_OTR,
            ED2_01_RM_ADM=ED2_01_RM_ADM,
            ED2_01_RM_ADH=ED2_01_RM_ADH,
            ED2_01_RM_ADOTR=ED2_01_RM_ADOTR,
            ED2_01_RM_MM=ED2_01_RM_MM,
            ED2_01_RM_HH=ED2_01_RM_HH,
            ED2_01_RM_DS=ED2_01_RM_DS,
            ED2_01_CA_NA=ED2_01_CA_NA,
            ED2_01_CA_NN=ED2_01_CA_NN,
            ED2_01_CA_OTR=ED2_01_CA_OTR,
            ED2_01_CA_ADM=ED2_01_CA_ADM,
            ED2_01_CA_ADH=ED2_01_CA_ADH,
            ED2_01_CA_ADOTR=ED2_01_CA_ADOTR,
            ED2_01_CA_MM=ED2_01_CA_MM,
            ED2_01_CA_HH=ED2_01_CA_HH,
            ED2_01_CA_DS=ED2_01_CA_DS,
            ED2_01_CUAL=commentary,

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
