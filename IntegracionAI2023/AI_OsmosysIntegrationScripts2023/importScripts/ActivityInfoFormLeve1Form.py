#%%
import importlib
import osmosys.osmosys
import osmosys.Backups
import activityinfo
import pandas as pd
import model.modelAI
from IPython.display import display
import json
import os.path
from urllib.error import HTTPError

def importForm(month, month_number, year, test):
    print('------------------import form level 1----------------')
    aiToken = osmosys.osmosys.getToken()
    osmosysData = osmosys.osmosys.get_ai_form_level_1()
    parentFormsIds = osmosysData.parent_form_id.unique()
    formTotalDf = None
    for parentFormsId in parentFormsIds:
        client = activityinfo.Client(token=aiToken, base_url='https://www.activityinfo.org/resources')
        try:
            dbsJson = client.get_resource('form/{parentFormsId}/query'.format(parentFormsId=parentFormsId))
            dbsDf = pd.DataFrame.from_records(dbsJson)
            dbsDf['parent_form_id'] = parentFormsId
            if formTotalDf is None:
                formTotalDf = dbsDf
            else:
                formTotalDf = pd.concat([formTotalDf, dbsDf], axis=0)
        except Exception:
            print("Oops!  No existe el registro")
            print(Exception)

    # ordeno para quitar duplicados
    # filtro implementaciòn indirecta y doante acnur
    formPartnersDf = formTotalDf.loc[
        (formTotalDf['implementacion'] == 'Indirecta') & (formTotalDf['donante.@id'] == 'chgxs46l02wy5i81e')]
    formPartnersDfClean = formPartnersDf.sort_values('@lastEditTime', ascending=False).drop_duplicates(
        subset=['implementador.@id', 'ubicacion.@id', 'parent_form_id'])

    formPartnersDfClean = formPartnersDfClean[
        ["@id", "@lastEditTime", "implementacion", "donante.@id", "donante.org_nombre", "donante.donante",
         "implementador.@id",
         "implementador.org_nombre", "implementador.donante", "ubicacion.@id", "ubicacion.name", "ubicacion.code",
         "ubicacion.parent.name", "ubicacion.parent.code", "codigo_referencia", "parent_form_id"
         ]]
    ## hago merge para buscar cuales falta por crear
    mergeDf = pd.merge(osmosysData, formPartnersDfClean, how='left',
                       left_on=['parent_form_id', 'id_registro_imp', 'canton_id_registro'],
                       right_on=['parent_form_id', 'implementador.@id', 'ubicacion.@id'])
    formsToCreate = mergeDf[mergeDf['@id'].isna()]
    numberOfFormTocreate = formsToCreate.shape[0]

    ## creo las estructuras de datos
    changesList = []
    newIds = []
    for index, row in formsToCreate.iterrows():
        form = model.modelAI.Form(cqes12jkykah3la9='40038', org_user='cc9fjcdkyyk18oj6',
                                  implementacion='c89eykqkykc8oig1b',
                                  donante='chgxs46l02wy5i81e',
                                  implementador=row['id_registro_imp'],
                                  ubicacion=row['canton_id_registro'])
        newId = activityinfo.generate_id()
        newIds.append(newId)
        record = model.modelAI.Record(row['parent_form_id'], newId, None, form)
        changesList.append(record)

    changes = model.modelAI.Changes(changesList)
    finalJson = json.dumps(changes, default=model.modelAI.default)
    osmosys.Backups.do_backup('form1_canton_org', '', month, year, changesList, finalJson)
    ## send to AI
    try:
        if (numberOfFormTocreate > 0 ):
            client.post_resource('update', body=finalJson)
            print("Enviado a AI")
        else:
            print("nada por crear en AI")
    except HTTPError as e:
        code = e.response.status_code
        print('error:')
        print(code)
        print(e)

    print('------------------Fin import form level 1----------------')