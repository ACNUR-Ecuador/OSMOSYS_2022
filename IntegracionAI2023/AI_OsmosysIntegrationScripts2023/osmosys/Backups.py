import json
import os.path
import model.modelAI
import pandas as pd


def do_backup(indicatorCodeAI, indicatorIdsOsmosys, month, year, changesList, finalJson):
    changesBK = []
    print(type(changesList))
    for change in changesList:
        changesBK.append({
            'indicadorIA': indicatorCodeAI,
            'indicatorIdsOsmosys': str(indicatorIdsOsmosys),
            'formId': change.formId,
            'recordId': change.recordId
        })
    changesBKDf = pd.DataFrame(changesBK)
    # open text file
    folder = str(year) + '-' + month
    filename = indicatorCodeAI + '-' + str(indicatorIdsOsmosys)
    try:
        os.mkdir(folder)
    except Exception:
        pass
    text_file = open(os.path.join(folder, "data_" + filename + ".json"), "w")
    # write string to file
    n = text_file.write(finalJson)

    # close file
    text_file.close()
    print(" creado respaldo: " + text_file.name)

    changesBKDf.to_csv(os.path.join(folder, "new_ids_" + filename + ".csv"))
