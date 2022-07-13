import pandas as pd

import osmosys.osmosys
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
    indicatorCodeAI = 'PRPI1_01'
    print(
        '--------------------------------------' + indicatorCodeAI + '------------------------------------------------------')

    # busco matchs
    subformDf = osmosys.osmosys.getMatchSubforms(indicatorCodeAI)
    ## print(subformDf)
    indicatorIdsOsmosys = subformDf.osmosys_indicador_id.unique()
    subformSeries = subformDf.iloc[0]
    formIdAI = subformSeries.form_id
    parentFormIdAI = subformSeries.parent_form_id
    indicatorIdAI = subformSeries.indicator_id
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

    ## 2 fuentes por separado indicador 35
    indicatorIdsOsmosys35 = [35]
    ## contruyo las estructuras de datos
    changesList = []
    newIds = []
    for index, row in parentFormsIds.iterrows():
        # print(row)
        orgAcron = row.acronym
        cantonCode = row.canton_code
        df = osmosys.osmosys.getRefValues(year=year, month=month, orgOsmosys=orgAcron,
                                          indicatorsIdsOmosys=indicatorIdsOsmosys35, cantonCode=cantonCode)

        poblacion_meta = ['Refugiados/as y migrantes', 'Comunidad de acogida']

        PRPI1_01_RM_NA = int(df.loc[df['age_gender'] == 'NINAS'].iloc[0].value_a)
        PRPI1_01_RM_NN = int(df.loc[df['age_gender'] == 'NINOS'].iloc[0].value_a)
        PRPI1_01_RM_OTR = int(df.loc[df['age_gender'] == 'OTRO'].iloc[0].value_a)
        dfDiversidad = osmosys.osmosys.getRefLgbtiDiscapacitadosValues(year=year, month=month, orgOsmosys=orgAcron,
                                                                       indicatorsIdsOmosys=indicatorIdsOsmosys35,
                                                                       cantonCode=cantonCode)
        PRPI1_01_RM_LGBT = int(dfDiversidad.loc[(dfDiversidad['diversity_type'] == 'LGBTI') & (
                dfDiversidad['country_of_origin'] == 'VENEZUELA')].iloc[0].value_a)
        PRPI1_01_RM_DS = int(dfDiversidad.loc[(dfDiversidad['diversity_type'] == 'DISCAPACITADOS') & (
                dfDiversidad['country_of_origin'] == 'VENEZUELA')].iloc[0].value_a)

        dfCa = osmosys.osmosys.getCAValues(year=year, month=month, orgOsmosys=orgAcron,
                                           indicatorsIdsOmosys=indicatorIdsOsmosys35, cantonCode=cantonCode)
        PRPI1_01_CA_NA = int(dfCa.loc[df['age_gender'] == 'NINAS'].iloc[0].value_a)
        PRPI1_01_CA_NN = int(dfCa.loc[df['age_gender'] == 'NINOS'].iloc[0].value_a)
        PRPI1_01_CA_OTR = int(dfCa.loc[df['age_gender'] == 'OTRO'].iloc[0].value_a)

        PRPI1_01_CA_LGBT = int(dfDiversidad.loc[(dfDiversidad['diversity_type'] == 'LGBTI') & (
                dfDiversidad['country_of_origin'] == 'ECUADOR')].iloc[0].value_a)
        PRPI1_01_CA_DS = int(dfDiversidad.loc[(dfDiversidad['diversity_type'] == 'DISCAPACITADOS') & (
                dfDiversidad['country_of_origin'] == 'ECUADOR')].iloc[0].value_a)
        PRPI1_01_CUAL =  osmosys.osmosys.getCommentary(year=year, month=month, orgOsmosys=orgAcron,
                                                                   indicatorsIdsOmosys=indicatorIdsOsmosys35).iloc[
            0].value_a

        subform = model.modelAI.SubFormPRPI1_01_35(
            mes=month_number,
            colltmgkykvhxgij6=indicatorIdAI,
            rmrp='Si',
            covid='No',
            espacion_apoyo='No',
            poblacion_meta=poblacion_meta,
            modalidad_impl='cmo12q1kykwwosjka',  ## servicio
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

    indicatorIdsOsmosys85 = [85]
    ## contruyo las estructuras de datos
    for index, row in parentFormsIds.iterrows():
        # print(row)
        orgAcron = row.acronym
        cantonCode = row.canton_code
        df = osmosys.osmosys.getRefValues(year=year, month=month, orgOsmosys=orgAcron,
                                          indicatorsIdsOmosys=indicatorIdsOsmosys85, cantonCode=cantonCode)

        poblacion_meta = ['Refugiados/as y migrantes', 'Comunidad de acogida']

        PRPI1_01_RM_NA = int(df.loc[df['age_gender'] == 'NINAS'].iloc[0].value_a)
        PRPI1_01_RM_NN = int(df.loc[df['age_gender'] == 'NINOS'].iloc[0].value_a)
        PRPI1_01_RM_OTR = int(df.loc[df['age_gender'] == 'OTRO'].iloc[0].value_a)
        dfDiversidad = osmosys.osmosys.getRefLgbtiDiscapacitadosValues(year=year, month=month, orgOsmosys=orgAcron,
                                                                       indicatorsIdsOmosys=indicatorIdsOsmosys85,
                                                                       cantonCode=cantonCode)
        PRPI1_01_RM_LGBT = int(dfDiversidad.loc[(dfDiversidad['diversity_type'] == 'LGBTI') & (
                dfDiversidad['country_of_origin'] == 'VENEZUELA')].iloc[0].value_a)
        PRPI1_01_RM_DS = int(dfDiversidad.loc[(dfDiversidad['diversity_type'] == 'DISCAPACITADOS') & (
                dfDiversidad['country_of_origin'] == 'VENEZUELA')].iloc[0].value_a)

        dfCa = osmosys.osmosys.getCAValues(year=year, month=month, orgOsmosys=orgAcron,
                                           indicatorsIdsOmosys=indicatorIdsOsmosys85, cantonCode=cantonCode)
        PRPI1_01_CA_NA = int(dfCa.loc[df['age_gender'] == 'NINAS'].iloc[0].value_a)
        PRPI1_01_CA_NN = int(dfCa.loc[df['age_gender'] == 'NINOS'].iloc[0].value_a)
        PRPI1_01_CA_OTR = int(dfCa.loc[df['age_gender'] == 'OTRO'].iloc[0].value_a)

        PRPI1_01_CA_LGBT = int(dfDiversidad.loc[(dfDiversidad['diversity_type'] == 'LGBTI') & (
                dfDiversidad['country_of_origin'] == 'ECUADOR')].iloc[0].value_a)
        PRPI1_01_CA_DS = int(dfDiversidad.loc[(dfDiversidad['diversity_type'] == 'DISCAPACITADOS') & (
                dfDiversidad['country_of_origin'] == 'ECUADOR')].iloc[0].value_a)
        PRPI1_01_CUAL = commentary = osmosys.osmosys.getCommentary(year=year, month=month, orgOsmosys=orgAcron,
                                                                   indicatorsIdsOmosys=indicatorIdsOsmosys85).iloc[
            0].value_a
        usd_transfer = int(osmosys.osmosys.getCBIBudget(year=year, month=month, orgOsmosys=orgAcron,
                                                        indicatorsIdsOmosys=indicatorIdsOsmosys85,
                                                        cantonCode=cantonCode).iloc[0].budget)
        subform = model.modelAI.SubFormPRPI1_01_85(
            mes=month_number,
            colltmgkykvhxgij6=indicatorIdAI,
            rmrp='Si',
            covid='No',
            espacion_apoyo='No',
            poblacion_meta=poblacion_meta,
            modalidad_impl='cmo12q1kykwwosjka',  ## servicio
            mecanismos='c6gspwxkym0dbinm',
            transferencia='cib4u00kym0ixwew',
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

    ## creo respaldo
    changes = model.modelAI.Changes(changesList)
    finalJson = json.dumps(changes, default=model.modelAI.default)

    # open text file
    try:
        os.mkdir(month)
    except Exception:
        pass
    text_file = open(os.path.join(month, "data_" + indicatorCodeAI + ".json"), "w")
    # write string to file
    n = text_file.write(finalJson)

    # close file
    text_file.close()
    print(" creado respaldo: " + text_file.name)
    newIdsDict = {"newIds": newIds}
    newIdsDf = pd.DataFrame(newIdsDict)
    newIdsDf.to_csv(os.path.join(month, "new_ids_" + indicatorCodeAI + ".csv"))

    ## envio a AI
    if (test):
        print(
            '--------------------------------------' + indicatorCodeAI + '------------------------------------------------------')
    else:
        try:
            if len(changesList) > 0:
                print('se envia a AI')
                client = Client(token=osmosys.osmosys.getToken(), base_url='https://www.activityinfo.org/resources')
                client.post_resource(path='update', body=finalJson)
                print(
                    '--------------------------------------' + indicatorCodeAI + '------------------------------------------------------')
            else:
                print('nada que enviar a AI')
        except HTTPError as e:
            code = e.response.status_code
            print('error:')
            print(code)

            print(e)
