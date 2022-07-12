import pandas as pds
from urllib import parse
from sqlalchemy import create_engine
import configparser

def getToken():
    config = configparser.RawConfigParser()
    config.read('../config.propierties')
    details_dict = dict(config.items('ACTIVITYINFO'))
    token = details_dict.get('token')
    return token

def getOsmosysConnection():
    # Create an engine instance
    parametersDict = readParameters()
    conectionString = 'postgresql://{user}:{password}@{host}/{database}'.format(
        user=parametersDict.get('user'),
        password=parse.quote(parametersDict.get('password')),
        host=parametersDict.get('host'),
        database=parametersDict.get('database')
    )
    # print(conectionString)
    alchemyEngine = create_engine(
        conectionString
    )
    # Connect to PostgreSQL server
    return alchemyEngine.connect()


def readParameters():
    config = configparser.RawConfigParser()
    config.read('../config.propierties')
    details_dict = dict(config.items('OSMOSYS'))
    return details_dict


def get_ai_form_level_1():
    dbConnection = getOsmosysConnection()
    dataFrame = pds.read_sql("SELECT * from ai_integration.ai_form_level_1", dbConnection)
    return dataFrame


def get_ie_by_indicator_code(indicatorOsmosysCode, month):
    queryByIndicadorCode = '''
    SELECT
    ie.id ie_id, i.id indicador_id, i.code indicator_code, org.acronym, m.month , m.total_execution
    FROM
    osmosys.indicator_executions ie 
    INNER JOIN osmosys.indicators i on ie.performance_indicator_id=i.id and ie.state='ACTIVO'
    INNER JOIN osmosys.projects pr on ie.project_id=pr.id
    INNER JOIN osmosys.organizations org on pr.organization_id=org.id
    INNER JOIN osmosys.quarters q ON ie.id=q.indicator_execution_id and q.state='ACTIVO'
    INNER JOIN osmosys.months m ON q.id=m.quarter_id and m.state='ACTIVO'
    WHERE i.code='xxxindicatorOsmosysCode' and m.month = 'xxxMonth' AND m.total_execution>0
    '''

    query = queryByIndicadorCode.replace('xxxindicatorOsmosysCode', indicatorOsmosysCode).replace('xxxMonth', month)

    dbConnection = getOsmosysConnection()
    dataFrame = pds.read_sql(query, dbConnection)
    return dataFrame


def getIesByIndicatorsIdsOsmosysAndMonth(indicatorOsmosysId, month):
    queryByIndicadorCode = '''
    SELECT
    ie.id ie_id, i.id indicador_id, i.code indicator_code, org.acronym, m.month , m.total_execution
    FROM
    osmosys.indicator_executions ie 
    INNER JOIN osmosys.indicators i on ie.performance_indicator_id=i.id and ie.state='ACTIVO'
    INNER JOIN osmosys.projects pr on ie.project_id=pr.id
    INNER JOIN osmosys.organizations org on pr.organization_id=org.id
    INNER JOIN osmosys.quarters q ON ie.id=q.indicator_execution_id and q.state='ACTIVO'
    INNER JOIN osmosys.months m ON q.id=m.quarter_id and m.state='ACTIVO'
    WHERE i.id=xxxindicatorOsmosysId and m.month = 'xxxMonth' AND m.total_execution>0
    '''

    query = queryByIndicadorCode.replace('xxxindicatorOsmosysId', str(indicatorOsmosysId)).replace('xxxMonth', month)

    dbConnection = getOsmosysConnection()
    dataFrame = pds.read_sql(query, dbConnection)
    return dataFrame


def getMatchSubforms(codigoAI):
    query = '''
    SELECT * FROM ai_integration.forms s 
    WHERE s.codigo='codigoAI'
    '''
    queryFormated = query.replace('codigoAI', codigoAI)
    dbConnection = getOsmosysConnection()
    dataFrame = pds.read_sql(queryFormated, dbConnection)
    return dataFrame


def getIesPartnerCantonsByIndicatorsIdsOsmosysAndMonth(indicatorsIdsOmosys, month, year):
    queryByIndicadorCode = '''
    SELECT
	distinct
    ie.id ie_id, i.id indicador_id, i.code indicator_code, org.acronym, 
		aio.nombre ai_implementer,
		m.month ,
		m.total_execution, can.code canton_code,'EC'||can.code canton_ai_code, can.description canton,
		sum(iv.value ) total_canton
    FROM
    osmosys.indicator_executions ie 
    INNER JOIN osmosys.indicators i on ie.performance_indicator_id=i.id and ie.state='ACTIVO'
    INNER JOIN osmosys.projects pr on ie.project_id=pr.id
    INNER JOIN osmosys.organizations org on pr.organization_id=org.id
		INNER JOIN ai_integration.osmosys_ai_implementador_organi aio on org.id=aio.osmosys_id
    INNER JOIN osmosys.quarters q ON ie.id=q.indicator_execution_id and q.state='ACTIVO'
    INNER JOIN osmosys.months m ON q.id=m.quarter_id and m.state='ACTIVO'
		INNER JOIN osmosys.indicator_values iv on m.id=iv.month_id and iv.state='ACTIVO'
		INNER JOIN osmosys.cantones can on iv.canton_id=can.id
    WHERE 
         i.id in (XXXindicatorIds) and
        m.month = 'xxxMonth' and m.year = 'xxxYear'
    GROUP BY 1,2,3,4,5,6,7,8,9,10
	HAVING sum(iv.value ) >0
	ORDER BY org.acronym, canton_code
    '''
    indicatorsIdsOmosysStr = ','.join(str(x) for x in indicatorsIdsOmosys)
    query = queryByIndicadorCode \
        .replace('xxxYear', str(year)) \
        .replace('xxxMonth', month) \
        .replace('XXXindicatorIds', indicatorsIdsOmosysStr)
    dbConnection = getOsmosysConnection()
    dataFrame = pds.read_sql(query, dbConnection)
    return dataFrame


def getRefValues(year, month, orgOsmosys, indicatorsIdsOmosys, cantonCode):
    query = '''
    SELECT 
    v.ie_id	, ie.performance_indicator_id indicator_osmosys_id,
     v.month_id	,v.year	,v.month_year_order	,v.month	,
    org.id org_odmodyd_id,org.acronym osmosys_org, v.canton_code	,
    v.canton	,v.population_type	,v.age_gender	,
    sum(v.value) value_a
    FROM ai_integration.ai_adultos_ninos_ref_migrantes_ven v
    INNER JOIN osmosys.indicator_executions ie on v.ie_id=ie.id
    INNER JOIN osmosys.projects pr on ie.project_id=pr.id
    INNER JOIN osmosys.organizations org on pr.organization_id=org."id"
    WHERE v.year=xxxYear and v.month='xxxMonth' 
    and org.acronym='xxxOrg'
    and v.canton_code='XXXcantonCode'
    and ie.performance_indicator_id in (XXXindicatorIds)
    GROUP BY 1,2,3,4,5,6,7,8,9,10,11,12
    '''
    indicatorsIdsOmosysStr = ','.join(str(x) for x in indicatorsIdsOmosys)
    queryFormated = query.replace('xxxYear', str(year)).replace('xxxMonth', month).replace('xxxOrg',
                                                                                           orgOsmosys).replace(
        'XXXindicatorIds', indicatorsIdsOmosysStr).replace('XXXcantonCode', cantonCode)
    ## print(queryFormated)

    dbConnection = getOsmosysConnection()
    dataFrame = pds.read_sql(queryFormated, dbConnection)
    return dataFrame


def getRefLgbtiDiscapacitadosValues(year, month, orgOsmosys, indicatorsIdsOmosys, cantonCode):
    query = '''
    SELECT
    v.org, v.month, v.diversity_type, v.canton_code, v.country_of_origin, sum(v.value) as value_a
    FROM
    ai_integration.ai_lgbti_discapacitados_countre_origin v
    WHERE v.indicator_id in (XXXindicatorIds) and v.org='xxxOrg' AND v.year=xxxYear and v.month = 'xxxMonth' AND v.canton_code='XXXcantonCode'
    GROUP BY 1,2,3,4,5
    '''
    indicatorsIdsOmosysStr = ','.join(str(x) for x in indicatorsIdsOmosys)
    queryFormated = query.replace('xxxYear', str(year)).replace('xxxMonth', month).replace('xxxOrg',
                                                                                           orgOsmosys).replace(
        'XXXindicatorIds', indicatorsIdsOmosysStr).replace('XXXcantonCode', cantonCode)
    ## print(queryFormated)

    dbConnection = getOsmosysConnection()
    dataFrame = pds.read_sql(queryFormated, dbConnection)
    return dataFrame


def getCommentary(year, month, orgOsmosys, indicatorsIdsOmosys):
    query = '''
    SELECT
    v.org, 
    v.year, v.month, string_agg(v.commentary,',' ) as value_a
    FROM
    ai_integration.ai_commentary v
    WHERE v.indicator_id in (XXXindicatorIds) 
    and v.org='xxxOrg' 
    AND v.year=xxxYear and v.month = 'xxxMonth' 
    GROUP BY 1,2,3
    '''
    indicatorsIdsOmosysStr = ','.join(str(x) for x in indicatorsIdsOmosys)
    queryFormated = query.replace('xxxYear', str(year)).replace('xxxMonth', month).replace('xxxOrg',
                                                                                           orgOsmosys).replace(
        'XXXindicatorIds', indicatorsIdsOmosysStr)
    ## print(queryFormated)

    dbConnection = getOsmosysConnection()
    dataFrame = pds.read_sql(queryFormated, dbConnection)
    return dataFrame
