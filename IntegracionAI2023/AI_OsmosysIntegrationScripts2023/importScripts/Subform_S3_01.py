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
    indicatorCodeAI = 'S3_01'
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
    reportedDf = osmosys.osmosys.getIesPartnerCantonsByIndicatorsIdsOsmosysAndMonth(
        indicatorsIdsOmosys=indicatorIdsOsmosys,
        month=month, year=year)
    print('reported data: ' + str(reportedDf.shape[0]))
    parentFormsIds = FormProcessing.getParentsIds(parentFormIdAI=parentFormIdAI, reportedDf=reportedDf)

    ## contruyo las estructuras de datos
    changesList = []
    newIds = []
    ## los que  son servicios
    for index, row in parentFormsIds.iterrows():
        # print(row)
        orgAcron = row.acronym
        cantonCode = row.canton_code
        df = osmosys.osmosys.getRefValues(year=year, month=month, orgOsmosys=orgAcron,
                                          indicatorsIdsOmosys=indicatorIdsOsmosys, cantonCode=cantonCode)

        poblacion_meta = ["Refugiados/as y Migrantes", "Comunidad de acogida"]

        S3_01_RM_NA_PS = int(df.loc[df['age_gender'] == 'NINAS'].iloc[0].value_a)
        S3_01_RM_NN_PS = int(df.loc[df['age_gender'] == 'NINOS'].iloc[0].value_a)
        S3_01_RM_MM_PS = int(df.loc[df['age_gender'] == 'ADULTAS'].iloc[0].value_a)
        S3_01_RM_HH_PS = int(df.loc[df['age_gender'] == 'ADULTOS'].iloc[0].value_a)
        S3_01_RM_OTR_PS = int(df.loc[df['age_gender'] == 'OTRO'].iloc[0].value_a)
        dfDiversidad = osmosys.osmosys.getRefLgbtiDiscapacitadosValues(year=year, month=month, orgOsmosys=orgAcron,
                                                                       indicatorsIdsOmosys=indicatorIdsOsmosys,
                                                                       cantonCode=cantonCode)
        S3_01_RM_LGBT_PS = int(dfDiversidad.loc[(dfDiversidad['diversity_type'] == 'LGBTI') & (
                dfDiversidad['country_of_origin'] == 'VENEZUELA')].iloc[0].value_a)
        S3_01_RM_DS_PS = int(dfDiversidad.loc[(dfDiversidad['diversity_type'] == 'DISCAPACITADOS') & (
                dfDiversidad['country_of_origin'] == 'VENEZUELA')].iloc[0].value_a)

        dfCa = osmosys.osmosys.getCAValues(year=year, month=month, orgOsmosys=orgAcron,
                                           indicatorsIdsOmosys=indicatorIdsOsmosys, cantonCode=cantonCode)
        S3_01_CA_NA_PS = int(dfCa.loc[df['age_gender'] == 'NINAS'].iloc[0].value_a)
        S3_01_CA_NN_PS = int(dfCa.loc[df['age_gender'] == 'NINOS'].iloc[0].value_a)
        S3_01_CA_MM_PS = int(dfCa.loc[df['age_gender'] == 'ADULTAS'].iloc[0].value_a)
        S3_01_CA_HH_PS = int(dfCa.loc[df['age_gender'] == 'ADULTOS'].iloc[0].value_a)
        S3_01_CA_OTR_PS = int(dfCa.loc[df['age_gender'] == 'OTRO'].iloc[0].value_a)

        S3_01_CA_LGBT_PS = int(dfDiversidad.loc[(dfDiversidad['diversity_type'] == 'LGBTI') & (
                dfDiversidad['country_of_origin'] == 'ECUADOR')].iloc[0].value_a)
        S3_01_CA_DS_PS = int(dfDiversidad.loc[(dfDiversidad['diversity_type'] == 'DISCAPACITADOS') & (
                dfDiversidad['country_of_origin'] == 'ECUADOR')].iloc[0].value_a)
        commentary = osmosys.osmosys.getCommentary(year=year, month=month, orgOsmosys=orgAcron,
                                                   indicatorsIdsOmosys=indicatorIdsOsmosys).iloc[
            0].value_a

        subform = model.modelAI.SubFormS3_01(
            mes=month_number,
            colltmgkykvhxgij6=indicatorIdAI,
            rmrp='Si',
            poblacion_meta=poblacion_meta,
            modalidad_impl='cmo12q1kykwwosjka',  ## servicio
            tipo_atencion='Atención psicosocial',
            S3_01_RM_NA_PS=S3_01_RM_NA_PS,
            S3_01_RM_NN_PS=S3_01_RM_NN_PS,
            S3_01_RM_MM_PS=S3_01_RM_MM_PS,
            S3_01_RM_HH_PS=S3_01_RM_HH_PS,
            S3_01_RM_OTR_PS=S3_01_RM_OTR_PS,
            S3_01_RM_LGBT_PS=S3_01_RM_LGBT_PS,
            S3_01_RM_DS_PS=S3_01_RM_DS_PS,
            S3_01_CA_NA_PS=S3_01_CA_NA_PS,
            S3_01_CA_NN_PS=S3_01_CA_NN_PS,
            S3_01_CA_MM_PS=S3_01_CA_MM_PS,
            S3_01_CA_HH_PS=S3_01_CA_HH_PS,
            S3_01_CA_OTR_PS=S3_01_CA_OTR_PS,
            S3_01_CA_LGBT_PS=S3_01_CA_LGBT_PS,
            S3_01_CA_DS_PS=S3_01_CA_DS_PS,
            S3_01_CUAL=commentary
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
