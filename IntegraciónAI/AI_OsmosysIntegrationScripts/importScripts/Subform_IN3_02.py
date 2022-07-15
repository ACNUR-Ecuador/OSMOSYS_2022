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
    indicatorCodeAI = 'IN3_02'
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
        df = osmosys.osmosys.getTotalMonthByCanton(year=year, month=month, orgOsmosys=orgAcron,
                                                   indicatorsIdsOmosys=indicatorIdsOsmosys, cantonCode=cantonCode)
        IN3_02 = int(df.iloc[0].value_a)
        commentary = osmosys.osmosys.getCommentary(year=year, month=month, orgOsmosys=orgAcron,
                                                   indicatorsIdsOmosys=indicatorIdsOsmosys).iloc[0].value_a
        subform = model.modelAI.SubFormIN3_02(
            mes=month_number,
            colltmgkykvhxgij6=indicatorIdAI,
            rmrp='Si',
            covid='No',
            IN3_02=IN3_02,
            IN3_02_CUAL=commentary
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
