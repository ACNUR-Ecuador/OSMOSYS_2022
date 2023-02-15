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
    indicatorCodeAI = 'PR3_01'
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

    ## obtengo los parents de AI
    parentFormsIds = FormProcessing.getParentsIds(parentFormIdAI=parentFormIdAI, reportedDf=reportedDf)

    ## contruyo las estructuras de datos
    changesList = []
    newIds = []
    for index, row in parentFormsIds.iterrows():
        # print(row)
        orgAcron = row.acronym
        cantonCode = row.canton_code
        df = osmosys.osmosys.getRefValues(year=year, month=month, orgOsmosys=orgAcron,
                                          indicatorsIdsOmosys=indicatorIdsOsmosys, cantonCode=cantonCode)
        r = df.loc[df['age_gender'] == 'NINAS'].iloc[0].value_a

        PR3_01_RM_NA = int(df.loc[df['age_gender'] == 'NINAS'].iloc[0].value_a)
        PR3_01_RM_NN = int(df.loc[df['age_gender'] == 'NINOS'].iloc[0].value_a)
        PR3_01_RM_MM = int(df.loc[df['age_gender'] == 'ADULTAS'].iloc[0].value_a)
        PR3_01_RM_HH = int(df.loc[df['age_gender'] == 'ADULTOS'].iloc[0].value_a)
        PR3_01_RM_OTR = int(df.loc[df['age_gender'] == 'OTRO'].iloc[0].value_a)
        dfDiversidad = osmosys.osmosys.getRefLgbtiDiscapacitadosValues(year=year, month=month, orgOsmosys=orgAcron,
                                                                       indicatorsIdsOmosys=indicatorIdsOsmosys,
                                                                       cantonCode=cantonCode)
        # print(dfDiversidad)
        PR3_01_RM_LGBT = int(dfDiversidad.loc[(dfDiversidad['diversity_type'] == 'LGBTI') & (
                dfDiversidad['country_of_origin'] == 'VENEZUELA')].iloc[0].value_a)
        PR3_01_RM_DS = int(dfDiversidad.loc[(dfDiversidad['diversity_type'] == 'DISCAPACITADOS') & (
                dfDiversidad['country_of_origin'] == 'VENEZUELA')].iloc[0].value_a)
        commentary = osmosys.osmosys.getCommentary(year=year, month=month, orgOsmosys=orgAcron,
                                                   indicatorsIdsOmosys=indicatorIdsOsmosys).iloc[0].value_a
        PR3_01_CUAL = commentary
        usd_transfer = int(osmosys.osmosys.getCBIBudget(year=year, month=month, orgOsmosys=orgAcron,
                                                        indicatorsIdsOmosys=indicatorIdsOsmosys,
                                                        cantonCode=cantonCode).iloc[0].budget)
        print(usd_transfer)
        modalidad_impl = "c3jf6gykykwwemuk7"  # efectivo
        mecanismos = "cr04avekym0dbimj"  ## ventanilla
        transferencia = "Condicional"

        subform = model.modelAI.SubFormPR3_01(
            mes=month_number,
            colltmgkykvhxgij6=indicatorIdAI,
            rmrp='Si',
            poblacion_meta='Refugiados/as y migrantes',
            modalidad_impl=modalidad_impl,
            mecanismos=mecanismos,
            transferencia=transferencia,
            usd_transfer=usd_transfer,
            PR3_01_RM_NA=PR3_01_RM_NA,
            PR3_01_RM_NN=PR3_01_RM_NN,
            PR3_01_RM_MM=PR3_01_RM_MM,
            PR3_01_RM_HH=PR3_01_RM_HH,
            PR3_01_RM_OTR=PR3_01_RM_OTR,
            PR3_01_RM_LGBT=PR3_01_RM_LGBT,
            PR3_01_RM_DS=PR3_01_RM_DS,
            PR3_01_CUAL=PR3_01_CUAL
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
