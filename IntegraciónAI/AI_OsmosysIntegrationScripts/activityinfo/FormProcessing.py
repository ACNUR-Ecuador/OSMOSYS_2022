from activityinfo import Client
import pandas as pd
token = '4853b111264c139048271246a3fed788'

def getParentsIds(parentFormIdAI, reportedDf):
    client = Client(token=token, base_url='https://www.activityinfo.org/resources')
    ## get data from AI
    dbsJson = client.get_resource('form/{parentFormsId}/query'.format(parentFormsId=parentFormIdAI))
    dbsDf = pd.DataFrame.from_records(dbsJson)
    implementacionACNUR = dbsDf.loc[
        (dbsDf['implementacion'] == 'Indirecta') & (dbsDf['donante.org_nombre'] == 'Alto Comisionado de las Naciones Unidas para los Refugiados (ACNUR)')]
    joinedDfs = pd.merge(reportedDf, implementacionACNUR, how='inner',
                         left_on=['ai_implementer', 'canton_ai_code'],
                         right_on=['implementador.org_nombre', 'ubicacion.code'])
    ## quito los duplicados
    joinedFinalCleaned = joinedDfs.sort_values('@lastEditTime', ascending=False).drop_duplicates(
        subset=['implementador.@id', 'ubicacion.@id'])
    columnsToSelect = ['ie_id',
                       'indicador_id',
                       'indicator_code',
                       'acronym',
                       'ai_implementer',
                       'month',
                       'total_execution',
                       'canton_code',
                       'canton_ai_code',
                       'canton',
                       'total_canton',
                       '@id']

    joinedFinal = joinedFinalCleaned[columnsToSelect]

    return joinedFinal

