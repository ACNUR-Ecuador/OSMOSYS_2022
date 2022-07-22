import json

from requests.exceptions import HTTPError

import model.modelAI
import osmosys.osmosys
from activityinfo import Client
from activityinfo import FormProcessing
from activityinfo.id import generate_id
import osmosys.Backups

def importForm(month, month_number, year, test):
    indicatorCodeAI = 'CBI2_01'
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
    ## contruyo las estructuras de datos
    changesList = []
    newIds = []
    for index, row in parentFormsIds.iterrows():
        # print(row)
        orgAcron = row.acronym
        cantonCode = row.canton_code
        df = osmosys.osmosys.getRefValues(year=year, month=month, orgOsmosys=orgAcron,
                                          indicatorsIdsOmosys=indicatorIdsOsmosys, cantonCode=cantonCode)

        poblacion_meta = ["Refugiados/as y Migrantes", "Comunidad de acogida"]

        CBI2_01_RM_NA_PR = int(df.loc[df['age_gender'] == 'NINAS'].iloc[0].value_a)
        CBI2_01_RM_NN_PR = int(df.loc[df['age_gender'] == 'NINOS'].iloc[0].value_a)
        CBI2_01_RM_MM_PR = int(df.loc[df['age_gender'] == 'ADULTAS'].iloc[0].value_a)
        CBI2_01_RM_HH_PR = int(df.loc[df['age_gender'] == 'ADULTOS'].iloc[0].value_a)

        dfCa = osmosys.osmosys.getCAValues(year=year, month=month, orgOsmosys=orgAcron,
                                           indicatorsIdsOmosys=indicatorIdsOsmosys, cantonCode=cantonCode)
        CBI2_01_CA_NA_PR = int(dfCa.loc[df['age_gender'] == 'NINAS'].iloc[0].value_a)
        CBI2_01_CA_NN_PR = int(dfCa.loc[df['age_gender'] == 'NINOS'].iloc[0].value_a)
        CBI2_01_CA_MM_PR = int(dfCa.loc[df['age_gender'] == 'ADULTAS'].iloc[0].value_a)
        CBI2_01_CA_HH_PR = int(dfCa.loc[df['age_gender'] == 'ADULTOS'].iloc[0].value_a)

        commentary = osmosys.osmosys.getCommentary(year=year, month=month, orgOsmosys=orgAcron,
                                                   indicatorsIdsOmosys=indicatorIdsOsmosys).iloc[
            0].value_a

        usd_transfer = int(osmosys.osmosys.getCBIBudget(year=year, month=month, orgOsmosys=orgAcron,
                                                        indicatorsIdsOmosys=indicatorIdsOsmosys,
                                                        cantonCode=cantonCode).iloc[0].budget)

        subform = model.modelAI.SubFormACBI2_01(
            mes=month_number,
            colltmgkykvhxgij6=indicatorIdAI,
            rmrp='Si',
            covid='No',
            espacion_apoyo='No',
            poblacion_meta=poblacion_meta,
            poblacion_meta_freq='Primera vez',
            modalidad_impl='c3jf6gykykwwemuk7',  ## EFECTIVO
            mecanismos='c3de3pmkym0dbino',  ## cajero
            transferencia='Incondicional',
            usd_transfer=usd_transfer,
            CBI2_01_RM_NA_PR=CBI2_01_RM_NA_PR,
            CBI2_01_RM_NN_PR=CBI2_01_RM_NN_PR,
            CBI2_01_RM_MM_PR=CBI2_01_RM_MM_PR,
            CBI2_01_RM_HH_PR=CBI2_01_RM_HH_PR,
            CBI2_01_CA_NA_PR=CBI2_01_CA_NA_PR,
            CBI2_01_CA_NN_PR=CBI2_01_CA_NN_PR,
            CBI2_01_CA_MM_PR=CBI2_01_CA_MM_PR,
            CBI2_01_CA_HH_PR=CBI2_01_CA_HH_PR,
            CBI2_01_CUAL=commentary

        )
        newId = generate_id()
        newIds.append(newId)
        record = model.modelAI.Record(formId=formIdAI, recordId=newId, parentRecordId=row['@id'], fields=subform)
        changesList.append(record)
    print('changes to charge: ' + str(len(changesList)))
    ## creo respaldo
    changes = model.modelAI.Changes(changesList)
    finalJson = json.dumps(changes, default=model.modelAI.default)
    ## creo respaldo
    osmosys.Backups.do_backup(indicatorCodeAI=indicatorCodeAI, indicatorIdsOsmosys=indicatorIdsOsmosys, month=month,
                              year=year, changesList=changesList, finalJson=finalJson)

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
