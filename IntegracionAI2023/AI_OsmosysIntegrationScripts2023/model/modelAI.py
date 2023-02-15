import json


def default(obj):
    if hasattr(obj, 'to_json'):
        return obj.to_json()
    raise TypeError(f'Object of type {obj.__class__.__name__} is not JSON serializable')


class Form:
    def __init__(self, cqes12jkykah3la9, org_user, implementacion, donante, implementador, ubicacion):
        self.cqes12jkykah3la9 = cqes12jkykah3la9
        self.org_user = org_user
        self.implementacion = implementacion
        self.donante = donante
        self.implementador = implementador
        self.ubicacion = ubicacion

    def __str__(self):
        to_return = {
            "cqes12jkykah3la9": self.cqes12jkykah3la9,
            "org_user": self.org_user,
            "implementacion": self.implementacion,
            "donante": self.donante,
            "implementador": self.implementador,
            "ubicacion": self.ubicacion
        }
        ## print(to_return)
        return to_return

    def __repr__(self):
        return self.__str__()

    def to_json(self):
        return self.__str__()


class Record:
    def __init__(self, formId, recordId, parentRecordId, fields):
        self.formId = formId
        self.recordId = recordId
        self.parentRecordId = parentRecordId
        self.fields = fields

    def __iter__(self):
        yield from {
            "formId": self.formId,
            "recordId": self.recordId,
            "parentRecordId": self.parentRecordId,
            "fields": self.fields
        }.items()

    def __str__(self):
        return json.dumps(dict(self), ensure_ascii=False)

    def __repr__(self):
        return self.__str__()

    def to_json(self):
        to_return = {
            "formId": self.formId,
            "recordId": self.recordId,
            "parentRecordId": self.parentRecordId
        }
        to_return["fields"] = self.fields.__dict__  # json.dumps(self.fields.to_json())
        ## print(to_return)
        return to_return


class Changes:
    def __init__(self, changes):
        self.changes = changes

    def __iter__(self):
        yield from {
            "changes": self.changes
        }.items()

    def __str__(self):
        return json.dumps(self.to_json())
        # return json.dumps(dict(self), default=default, ensure_ascii=False)

    def __repr__(self):
        return self.__str__()

    def to_json(self):
        jchanges = []
        for change in self.changes:
            jchanges.append(change.__dict__)

        return {"changes": jchanges}


def test():
    form1 = Form("test1", "org_user", "implementation_", "donante", "implementador_", "ubicacion")
    form2 = Form("test2", "org_user2", "implementation_2", "donante2", "implementador_2", "ubicacion2")

    record1 = Record("formId1", "recordId1", None, form1)
    record2 = Record("formId2", "recordId2", None, form2)

    ## print(json.dumps(record1, default=default))
    changesList = []
    changesList.append(record1)
    changesList.append(record2)
    changes = Changes(changesList)

    ##print(json.dumps(changes, default=default))


class SubFormPR2_02:
    def __init__(self,
                 mes,
                 colltmgkykvhxgij6,
                 rmrp,
                 covid,
                 espacion_apoyo,
                 poblacion_meta,
                 modalidad_impl,
                 PR2_02_RM_NA,
                 PR2_02_RM_NN,
                 PR2_02_RM_MM,
                 PR2_02_RM_HH,
                 PR2_02_RM_OTR,
                 PR2_02_RM_LGBT,
                 PR2_02_RM_DS,
                 PR2_02_CUAL
                 ):
        self.mes = mes
        self.colltmgkykvhxgij6 = colltmgkykvhxgij6
        self.rmrp = rmrp
        self.covid = covid
        self.espacion_apoyo = espacion_apoyo
        self.poblacion_meta = poblacion_meta
        self.modalidad_impl = modalidad_impl
        self.PR2_02_RM_NA = PR2_02_RM_NA
        self.PR2_02_RM_NN = PR2_02_RM_NN
        self.PR2_02_RM_MM = PR2_02_RM_MM
        self.PR2_02_RM_HH = PR2_02_RM_HH
        self.PR2_02_RM_OTR = PR2_02_RM_OTR
        self.PR2_02_RM_LGBT = PR2_02_RM_LGBT
        self.PR2_02_RM_DS = PR2_02_RM_DS
        self.PR2_02_CUAL = PR2_02_CUAL

    def __str__(self):
        to_return = {
            "mes": self.mes,
            "colltmgkykvhxgij6": self.colltmgkykvhxgij6,
            "rmrp": self.rmrp,
            "covid": self.covid,
            "espacion_apoyo": self.espacion_apoyo,
            "poblacion_meta": self.poblacion_meta,
            "modalidad_impl": self.modalidad_impl,
            "PR2_02_RM_NA": self.PR2_02_RM_NA,
            "PR2_02_RM_NN": self.PR2_02_RM_NN,
            "PR2_02_RM_MM": self.PR2_02_RM_MM,
            "PR2_02_RM_HH": self.PR2_02_RM_HH,
            "PR2_02_RM_OTR": self.PR2_02_RM_OTR,
            "PR2_02_RM_LGBT": self.PR2_02_RM_LGBT,
            "PR2_02_RM_DS": self.PR2_02_RM_DS,
            "PR2_02_CUAL": self.PR2_02_CUAL
        }
        return to_return

    def __repr__(self):
        return self.__str__()

    def to_json(self):
        return self.__str__()


class SubFormPR1_01:
    def __init__(self,
                 mes,
                 colltmgkykvhxgij6,
                 orientacion,
                 rmrp,
                 poblacion_meta,
                 PR1_01_RM_NA,
                 PR1_01_RM_NN,
                 PR1_01_RM_MM,
                 PR1_01_RM_HH,
                 PR1_01_RM_OTR,
                 PR1_01_RM_LGBT,
                 PR1_01_RM_DS,
                 PR1_01_CUAL,
                 PR1_02_RM_NA,
                 PR1_02_RM_NN,
                 PR1_02_RM_MM,
                 PR1_02_RM_HH,
                 PR1_02_RM_OTR,
                 PR1_02_RM_LGBT,
                 PR1_02_RM_DS,
                 PR1_02_CUAL
                 ):
        self.mes = mes
        self.colltmgkykvhxgij6 = colltmgkykvhxgij6
        self.orientacion = orientacion
        self.rmrp = rmrp
        self.poblacion_meta = poblacion_meta
        self.PR1_01_RM_NA = PR1_01_RM_NA
        self.PR1_01_RM_NN = PR1_01_RM_NN
        self.PR1_01_RM_MM = PR1_01_RM_MM
        self.PR1_01_RM_HH = PR1_01_RM_HH
        self.PR1_01_RM_OTR = PR1_01_RM_OTR
        self.PR1_01_RM_LGBT = PR1_01_RM_LGBT
        self.PR1_01_RM_DS = PR1_01_RM_DS
        self.PR1_01_CUAL = PR1_01_CUAL
        self.PR1_02_RM_NA = PR1_02_RM_NA
        self.PR1_02_RM_NN = PR1_02_RM_NN
        self.PR1_02_RM_MM = PR1_02_RM_MM
        self.PR1_02_RM_HH = PR1_02_RM_HH
        self.PR1_02_RM_OTR = PR1_02_RM_OTR
        self.PR1_02_RM_LGBT = PR1_02_RM_LGBT
        self.PR1_02_RM_DS = PR1_02_RM_DS
        self.PR1_02_CUAL = PR1_02_CUAL

    def __str__(self):
        to_return = {
            "mes": self.mes,
            "colltmgkykvhxgij6": self.colltmgkykvhxgij6,
            "orientacion": self.orientacion,
            "rmrp": self.rmrp,
            "poblacion_meta": self.poblacion_meta,
            "PR1_01_RM_NA": self.PR1_01_RM_NA,
            "PR1_01_RM_NN": self.PR1_01_RM_NN,
            "PR1_01_RM_MM": self.PR1_01_RM_MM,
            "PR1_01_RM_HH": self.PR1_01_RM_HH,
            "PR1_01_RM_OTR": self.PR1_01_RM_OTR,
            "PR1_01_RM_LGBT": self.PR1_01_RM_LGBT,
            "PR1_01_RM_DS": self.PR1_01_RM_DS,
            "PR1_01_CUAL": self.PR1_01_CUAL,
            "PR1_02_RM_NA": self.PR1_02_RM_NA,
            "PR1_02_RM_NN": self.PR1_02_RM_NN,
            "PR1_02_RM_MM": self.PR1_02_RM_MM,
            "PR1_02_RM_HH": self.PR1_02_RM_HH,
            "PR1_02_RM_OTR": self.PR1_02_RM_OTR,
            "PR1_02_RM_LGBT": self.PR1_02_RM_LGBT,
            "PR1_02_RM_DS": self.PR1_02_RM_DS,
            "PR1_02_CUAL": self.PR1_02_CUAL,
        }
        return to_return

    def __repr__(self):
        return self.__str__()

    def to_json(self):
        return self.__str__()


class SubFormPR2_02:
    def __init__(self,
                 mes,
                 colltmgkykvhxgij6,
                 rmrp,
                 covid,
                 espacion_apoyo,
                 poblacion_meta,
                 modalidad_impl,
                 PR2_02_RM_NA,
                 PR2_02_RM_NN,
                 PR2_02_RM_MM,
                 PR2_02_RM_HH,
                 PR2_02_RM_OTR,
                 PR2_02_RM_LGBT,
                 PR2_02_RM_DS,
                 PR2_02_CUAL
                 ):
        self.mes = mes
        self.colltmgkykvhxgij6 = colltmgkykvhxgij6
        self.rmrp = rmrp
        self.covid = covid
        self.espacion_apoyo = espacion_apoyo
        self.poblacion_meta = poblacion_meta
        self.modalidad_impl = modalidad_impl
        self.PR2_02_RM_NA = PR2_02_RM_NA
        self.PR2_02_RM_NN = PR2_02_RM_NN
        self.PR2_02_RM_MM = PR2_02_RM_MM
        self.PR2_02_RM_HH = PR2_02_RM_HH
        self.PR2_02_RM_OTR = PR2_02_RM_OTR
        self.PR2_02_RM_LGBT = PR2_02_RM_LGBT
        self.PR2_02_RM_DS = PR2_02_RM_DS
        self.PR2_02_CUAL = PR2_02_CUAL

    def __str__(self):
        to_return = {
            "mes": self.mes,
            "colltmgkykvhxgij6": self.colltmgkykvhxgij6,
            "rmrp": self.rmrp,
            "covid": self.covid,
            "espacion_apoyo": self.espacion_apoyo,
            "poblacion_meta": self.poblacion_meta,
            "modalidad_impl": self.modalidad_impl,
            "PR2_02_RM_NA": self.PR2_02_RM_NA,
            "PR2_02_RM_NN": self.PR2_02_RM_NN,
            "PR2_02_RM_MM": self.PR2_02_RM_MM,
            "PR2_02_RM_HH": self.PR2_02_RM_HH,
            "PR2_02_RM_OTR": self.PR2_02_RM_OTR,
            "PR2_02_RM_LGBT": self.PR2_02_RM_LGBT,
            "PR2_02_RM_DS": self.PR2_02_RM_DS,
            "PR2_02_CUAL": self.PR2_02_CUAL
        }
        return to_return

    def __repr__(self):
        return self.__str__()

    def to_json(self):
        return self.__str__()


class SubFormPR3_01:
    def __init__(self,
                 mes,
                 colltmgkykvhxgij6,
                 rmrp,
                 poblacion_meta,
                 modalidad_impl,
                 mecanismos,
                 transferencia,
                 usd_transfer,
                 PR3_01_RM_NA,
                 PR3_01_RM_NN,
                 PR3_01_RM_MM,
                 PR3_01_RM_HH,
                 PR3_01_RM_OTR,
                 PR3_01_RM_LGBT,
                 PR3_01_RM_DS,
                 PR3_01_CUAL
                 ):
        self.mes = mes
        self.colltmgkykvhxgij6 = colltmgkykvhxgij6
        self.rmrp = rmrp
        self.covid = covid
        self.espacion_apoyo = espacion_apoyo
        self.poblacion_meta = poblacion_meta
        self.modalidad_impl = modalidad_impl
        self.mecanismos = mecanismos
        self.transferencia = transferencia
        self.usd_transfer = usd_transfer
        self.PR3_01_RM_NA = PR3_01_RM_NA
        self.PR3_01_RM_NN = PR3_01_RM_NN
        self.PR3_01_RM_MM = PR3_01_RM_MM
        self.PR3_01_RM_HH = PR3_01_RM_HH
        self.PR3_01_RM_OTR = PR3_01_RM_OTR
        self.PR3_01_RM_LGBT = PR3_01_RM_LGBT
        self.PR3_01_RM_DS = PR3_01_RM_DS
        self.PR3_01_CUAL = PR3_01_CUAL

    def __str__(self):
        to_return = {
            "mes": self.mes,
            "colltmgkykvhxgij6": self.colltmgkykvhxgij6,
            "rmrp": self.rmrp,
            "covid": self.covid,
            "espacion_apoyo": self.espacion_apoyo,
            "poblacion_meta": self.poblacion_meta,
            "modalidad_impl": self.modalidad_impl,
            "mecanismos": self.mecanismos,
            "transferencia": self.transferencia,
            "usd_transfer": self.usd_transfer,
            "PR3_01_RM_NA": self.PR3_01_RM_NA,
            "PR3_01_RM_NN": self.PR3_01_RM_NN,
            "PR3_01_RM_MM": self.PR3_01_RM_MM,
            "PR3_01_RM_HH": self.PR3_01_RM_HH,
            "PR3_01_RM_OTR": self.PR3_01_RM_OTR,
            "PR3_01_RM_LGBT": self.PR3_01_RM_LGBT,
            "PR3_01_RM_DS": self.PR3_01_RM_DS,
            "PR3_01_CUAL": self.PR3_01_CUAL
        }
        return to_return

    def __repr__(self):
        return self.__str__()

    def to_json(self):
        return self.__str__()


class SubFormPR4_03:
    def __init__(self,
                 mes,
                 colltmgkykvhxgij6,
                 rmrp,
                 PR4_03,
                 PR4_03_CUAL
                 ):
        self.mes = mes
        self.colltmgkykvhxgij6 = colltmgkykvhxgij6
        self.rmrp = rmrp
        self.PR4_03 = PR4_03
        self.PR4_03_CUAL = PR4_03_CUAL

    def __str__(self):
        to_return = {
            "mes": self.mes,
            "colltmgkykvhxgij6": self.colltmgkykvhxgij6,
            "rmrp": self.rmrp,
            "PR4_03": self.PR4_03,
            "PR4_03_CUAL": self.PR4_03_CUAL
        }
        return to_return

    def __repr__(self):
        return self.__str__()

    def to_json(self):
        return self.__str__()


class SubFormPR6_01:
    def __init__(self,
                 mes,
                 colltmgkykvhxgij6,
                 rmrp,
                 modalidad_impl,
                 PR6_01,
                 PR6_01_CUAL
                 ):
        self.mes = mes,
        self.colltmgkykvhxgij6 = colltmgkykvhxgij6,
        self.rmrp = rmrp,
        self.modalidad_impl = modalidad_impl,
        self.PR6_01 = PR6_01,
        self.PR6_01_CUAL = PR6_01_CUAL

    def __str__(self):
        to_return = {
            "mes": self.mes,
            "colltmgkykvhxgij6": self.colltmgkykvhxgij6,
            "rmrp": self.rmrp,
            "modalidad_impl": self.modalidad_impl,
            "PR6_01": self.PR6_01,
            "PR6_01_CUAL": self.PR6_01_CUAL
        }
        return to_return

    def __repr__(self):
        return self.__str__()

    def to_json(self):
        return self.__str__()


class SubFormPRPI1_01:
    def __init__(self,
                 mes,
                 colltmgkykvhxgij6,
                 rmrp,
                 poblacion_meta,
                 modalidad_impl,
                 mecanismos,
                 transferencia,
                 usd_transfer,
                 PRPI1_01_RM_NA,
                 PRPI1_01_RM_NN,
                 PRPI1_01_RM_OTR,
                 PRPI1_01_RM_LGBT,
                 PRPI1_01_RM_DS,
                 PRPI1_01_CA_NA,
                 PRPI1_01_CA_NN,
                 PRPI1_01_CA_OTR,
                 PRPI1_01_CA_LGBT,
                 PRPI1_01_CA_DS,
                 PRPI1_01_CUAL
                 ):
        self.mes = mes
        self.colltmgkykvhxgij6 = colltmgkykvhxgij6
        self.rmrp = rmrp
        self.poblacion_meta = poblacion_meta
        self.modalidad_impl = modalidad_impl
        self.mecanismos = mecanismos
        self.transferencia = transferencia
        self.usd_transfer = usd_transfer
        self.PRPI1_01_RM_NA = PRPI1_01_RM_NA
        self.PRPI1_01_RM_NN = PRPI1_01_RM_NN
        self.PRPI1_01_RM_OTR = PRPI1_01_RM_OTR
        self.PRPI1_01_RM_LGBT = PRPI1_01_RM_LGBT
        self.PRPI1_01_RM_DS = PRPI1_01_RM_DS
        self.PRPI1_01_CA_NA = PRPI1_01_CA_NA
        self.PRPI1_01_CA_NN = PRPI1_01_CA_NN
        self.PRPI1_01_CA_OTR = PRPI1_01_CA_OTR
        self.PRPI1_01_CA_LGBT = PRPI1_01_CA_LGBT
        self.PRPI1_01_CA_DS = PRPI1_01_CA_DS
        self.PRPI1_01_CUAL = PRPI1_01_CUAL

    def __str__(self):
        to_return = {
            "mes": self.mes,
            "colltmgkykvhxgij6": self.colltmgkykvhxgij6,
            "rmrp": self.rmrp,
            "poblacion_meta": self.poblacion_meta,
            "modalidad_impl": self.modalidad_impl,
            "mecanismos": self.mecanismos,
            "transferencia": self.transferencia,
            "usd_transfer": self.usd_transfer,
            "PRPI1_01_RM_NA": self.PRPI1_01_RM_NA,
            "PRPI1_01_RM_NN": self.PRPI1_01_RM_NN,
            "PRPI1_01_RM_OTR": self.PRPI1_01_RM_OTR,
            "PRPI1_01_RM_LGBT": self.PRPI1_01_RM_LGBT,
            "PRPI1_01_RM_DS": self.PRPI1_01_RM_DS,
            "PRPI1_01_CA_NA": self.PRPI1_01_CA_NA,
            "PRPI1_01_CA_NN": self.PRPI1_01_CA_NN,
            "PRPI1_01_CA_OTR": self.PRPI1_01_CA_OTR,
            "PRPI1_01_CA_LGBT": self.PRPI1_01_CA_LGBT,
            "PRPI1_01_CA_DS": self.PRPI1_01_CA_DS,
            "PRPI1_01_CUAL": self.PRPI1_01_CUAL
        }
        return to_return

    def __repr__(self):
        return self.__str__()

    def to_json(self):
        return self.__str__()



class SubFormPRPI1_02:
    def __init__(self,
                 mes,
                 colltmgkykvhxgij6,
                 rmrp,
                 poblacion_meta2,
                 PRPI1_02_RM_NA,
                 PRPI1_02_RM_NN,
                 PRPI1_02_RM_OTR,
                 PRPI1_02_RM_LGBT,
                 PRPI1_02_RM_DS,
                 PRPI1_02_CUAL
                 ):
        self.mes = mes
        self.colltmgkykvhxgij6 = colltmgkykvhxgij6
        self.rmrp = rmrp
        self.poblacion_meta2 = poblacion_meta2
        self.PRPI1_02_RM_NA = PRPI1_02_RM_NA
        self.PRPI1_02_RM_NN = PRPI1_02_RM_NN
        self.PRPI1_02_RM_OTR = PRPI1_02_RM_OTR
        self.PRPI1_02_RM_LGBT = PRPI1_02_RM_LGBT
        self.PRPI1_02_RM_DS = PRPI1_02_RM_DS
        self.PRPI1_02_CUAL = PRPI1_02_CUAL

    def __str__(self):
        to_return = {
            "mes": self.mes,
            "colltmgkykvhxgij6": self.colltmgkykvhxgij6,
            "rmrp": self.rmrp,
            "poblacion_meta2": self.poblacion_meta2,
            "PRPI1_02_RM_NA": self.PRPI1_02_RM_NA,
            "PRPI1_02_RM_NN": self.PRPI1_02_RM_NN,
            "PRPI1_02_RM_OTR": self.PRPI1_02_RM_OTR,
            "PRPI1_02_RM_LGBT": self.PRPI1_02_RM_LGBT,
            "PRPI1_02_RM_DS": self.PRPI1_02_RM_DS,
            "PRPI1_02_CUAL": self.PRPI1_02_CUAL
        }
        return to_return

    def __repr__(self):
        return self.__str__()

    def to_json(self):
        return self.__str__()


class SubFormPRPI5_01:
    def __init__(self,
                 mes,
                 colltmgkykvhxgij6,
                 rmrp,
                 covid,
                 PRPI5_01,
                 PRPI5_01_CUAL
                 ):
        self.mes = mes
        self.colltmgkykvhxgij6 = colltmgkykvhxgij6
        self.rmrp = rmrp
        self.covid = covid
        self.PRPI5_01 = PRPI5_01
        self.PRPI5_01_CUAL = PRPI5_01_CUAL

    def __str__(self):
        to_return = {
            "mes": self.mes,
            "colltmgkykvhxgij6": self.colltmgkykvhxgij6,
            "rmrp": self.rmrp,
            "covid": self.covid,
            "PRPI5_01": self.PRPI5_01,
            "PRPI5_01_CUAL": self.PRPI5_01_CUAL
        }
        return to_return

    def __repr__(self):
        return self.__str__()

    def to_json(self):
        return self.__str__()


class SubFormPRPI5_03:
    def __init__(self,
                 mes,
                 colltmgkykvhxgij6,
                 rmrp,
                 covid,
                 PRPI5_03,
                 PRPI5_03_CUAL
                 ):
        self.mes = mes
        self.colltmgkykvhxgij6 = colltmgkykvhxgij6
        self.rmrp = rmrp
        self.covid = covid
        self.PRPI5_03 = PRPI5_03
        self.PRPI5_03_CUAL = PRPI5_03_CUAL

    def __str__(self):
        to_return = {
            "mes": self.mes,
            "colltmgkykvhxgij6": self.colltmgkykvhxgij6,
            "rmrp": self.rmrp,
            "covid": self.covid,
            "PRPI5_03": self.PRPI5_03,
            "PRPI5_03_CUAL": self.PRPI5_03_CUAL
        }
        return to_return

    def __repr__(self):
        return self.__str__()

    def to_json(self):
        return self.__str__()


class SubFormPRVBG2_01:
    def __init__(self,
                 mes,
                 colltmgkykvhxgij6,
                 rmrp,
                 covid,
                 poblacion_meta,
                 modalidad_impl,
                 PRVBG2_01_RM_NA,
                 PRVBG2_01_RM_NN,
                 PRVBG2_01_RM_MM,
                 PRVBG2_01_RM_HH,
                 PRVBG2_01_RM_OTR,
                 PRVBG2_01_RM_LGBT,
                 PRVBG2_01_RM_DS,
                 PRVBG2_01_CA_NA,
                 PRVBG2_01_CA_NN,
                 PRVBG2_01_CA_MM,
                 PRVBG2_01_CA_HH,
                 PRVBG2_01_CA_OTR,
                 PRVBG2_01_CA_LGBT,
                 PRVBG2_01_CA_DS,
                 PRVBG2_01_CUAL

                 ):
        self.mes = mes
        self.colltmgkykvhxgij6 = colltmgkykvhxgij6
        self.rmrp = rmrp
        self.covid = covid
        self.poblacion_meta = poblacion_meta
        self.modalidad_impl = modalidad_impl
        self.PRVBG2_01_RM_NA = PRVBG2_01_RM_NA
        self.PRVBG2_01_RM_NN = PRVBG2_01_RM_NN
        self.PRVBG2_01_RM_MM = PRVBG2_01_RM_MM
        self.PRVBG2_01_RM_HH = PRVBG2_01_RM_HH
        self.PRVBG2_01_RM_OTR = PRVBG2_01_RM_OTR
        self.PRVBG2_01_RM_LGBT = PRVBG2_01_RM_LGBT
        self.PRVBG2_01_RM_DS = PRVBG2_01_RM_DS
        self.PRVBG2_01_CA_NA = PRVBG2_01_CA_NA
        self.PRVBG2_01_CA_NN = PRVBG2_01_CA_NN
        self.PRVBG2_01_CA_MM = PRVBG2_01_CA_MM
        self.PRVBG2_01_CA_HH = PRVBG2_01_CA_HH
        self.PRVBG2_01_CA_OTR = PRVBG2_01_CA_OTR
        self.PRVBG2_01_CA_LGBT = PRVBG2_01_CA_LGBT
        self.PRVBG2_01_CA_DS = PRVBG2_01_CA_DS
        self.PRVBG2_01_CUAL = PRVBG2_01_CUAL

    def __str__(self):
        to_return = {
            "mes": self.mes,
            "colltmgkykvhxgij6": self.colltmgkykvhxgij6,
            "rmrp": self.rmrp,
            "covid": self.covid,
            "poblacion_meta": self.poblacion_meta,
            "modalidad_impl": self.modalidad_impl,
            "PRVBG2_01_RM_NA": self.PRVBG2_01_RM_NA,
            "PRVBG2_01_RM_NN": self.PRVBG2_01_RM_NN,
            "PRVBG2_01_RM_MM": self.PRVBG2_01_RM_MM,
            "PRVBG2_01_RM_HH": self.PRVBG2_01_RM_HH,
            "PRVBG2_01_RM_OTR": self.PRVBG2_01_RM_OTR,
            "PRVBG2_01_RM_LGBT": self.PRVBG2_01_RM_LGBT,
            "PRVBG2_01_RM_DS": self.PRVBG2_01_RM_DS,
            "PRVBG2_01_CA_NA": self.PRVBG2_01_CA_NA,
            "PRVBG2_01_CA_NN": self.PRVBG2_01_CA_NN,
            "PRVBG2_01_CA_MM": self.PRVBG2_01_CA_MM,
            "PRVBG2_01_CA_HH": self.PRVBG2_01_CA_HH,
            "PRVBG2_01_CA_OTR": self.PRVBG2_01_CA_OTR,
            "PRVBG2_01_CA_LGBT": self.PRVBG2_01_CA_LGBT,
            "PRVBG2_01_CA_DS": self.PRVBG2_01_CA_DS,
            "PRVBG2_01_CUAL": self.PRVBG2_01_CUAL
        }
        return to_return

    def __repr__(self):
        return self.__str__()

    def to_json(self):
        return self.__str__()


class SubFormPRVBG4_01:
    def __init__(self,
                 mes,
                 colltmgkykvhxgij6,
                 rmrp,
                 covid,
                 PRVBG4_01,
                 PRVBG4_01_CUAL
                 ):
        self.mes = mes
        self.colltmgkykvhxgij6 = colltmgkykvhxgij6
        self.rmrp = rmrp
        self.covid = covid
        self.PRVBG4_01 = PRVBG4_01
        self.PRVBG4_01_CUAL = PRVBG4_01_CUAL

    def __str__(self):
        to_return = {
            "mes": self.mes,
            "colltmgkykvhxgij6": self.colltmgkykvhxgij6,
            "rmrp": self.rmrp,
            "covid": self.covid,
            "PRVBG4_01": self.PRVBG4_01,
            "PRVBG4_01_CUAL": self.PRVBG4_01_CUAL
        }
        return to_return

    def __repr__(self):
        return self.__str__()

    def to_json(self):
        return self.__str__()


class SubFormPRVBG4_03:
    def __init__(self,
                 mes,
                 colltmgkykvhxgij6,
                 rmrp,
                 covid,
                 PRVBG4_03,
                 PRVBG4_03_CUAL
                 ):
        self.mes = mes
        self.colltmgkykvhxgij6 = colltmgkykvhxgij6
        self.rmrp = rmrp
        self.covid = covid
        self.PRVBG4_03 = PRVBG4_03
        self.PRVBG4_03_CUAL = PRVBG4_03_CUAL

    def __str__(self):
        to_return = {
            "mes": self.mes,
            "colltmgkykvhxgij6": self.colltmgkykvhxgij6,
            "rmrp": self.rmrp,
            "covid": self.covid,
            "PRVBG4_03": self.PRVBG4_03,
            "PRVBG4_03_CUAL": self.PRVBG4_03_CUAL
        }
        return to_return

    def __repr__(self):
        return self.__str__()

    def to_json(self):
        return self.__str__()


class SubFormS4_01:
    def __init__(self,
                 mes,
                 colltmgkykvhxgij6,
                 rmrp,
                 covid,
                 accion,
                 S4_01_S,
                 S4_01_CUAL
                 ):
        self.mes = mes
        self.colltmgkykvhxgij6 = colltmgkykvhxgij6
        self.rmrp = rmrp
        self.covid = covid
        self.accion = accion
        self.S4_01_S = S4_01_S
        self.S4_01_CUAL = S4_01_CUAL

    def __str__(self):
        to_return = {
            "mes": self.mes,
            "colltmgkykvhxgij6": self.colltmgkykvhxgij6,
            "rmrp": self.rmrp,
            "covid": self.covid,
            "accion": self.accion,
            "S4_01_S": self.S4_01_S,
            "S4_01_CUAL": self.S4_01_CUAL
        }
        return to_return

    def __repr__(self):
        return self.__str__()

    def to_json(self):
        return self.__str__()


class SubFormWA1_01:
    def __init__(self,
                 mes,
                 colltmgkykvhxgij6,
                 rmrp,
                 covid,
                 wash_individual,
                 poblacion_meta,
                 modalidad_impl,
                 WA1_01_RM_NA,
                 WA1_01_RM_NN,
                 WA1_01_RM_MM,
                 WA1_01_RM_HH,
                 WA1_01_CA_NA,
                 WA1_01_CA_NN,
                 WA1_01_CA_MM,
                 WA1_01_CA_HH,
                 WA1_01_CUAL
                 ):
        self.mes = mes
        self.colltmgkykvhxgij6 = colltmgkykvhxgij6
        self.rmrp = rmrp
        self.covid = covid
        self.wash_individual = wash_individual
        self.poblacion_meta = poblacion_meta
        self.modalidad_impl = modalidad_impl
        self.WA1_01_RM_NA = WA1_01_RM_NA
        self.WA1_01_RM_NN = WA1_01_RM_NN
        self.WA1_01_RM_MM = WA1_01_RM_MM
        self.WA1_01_RM_HH = WA1_01_RM_HH
        self.WA1_01_CA_NA = WA1_01_CA_NA
        self.WA1_01_CA_NN = WA1_01_CA_NN
        self.WA1_01_CA_MM = WA1_01_CA_MM
        self.WA1_01_CA_HH = WA1_01_CA_HH
        self.WA1_01_CUAL = WA1_01_CUAL

    def __str__(self):
        to_return = {
            "mes": self.mes,
            "colltmgkykvhxgij6": self.colltmgkykvhxgij6,
            "rmrp": self.rmrp,
            "covid": self.covid,
            "wash_individual": self.wash_individual,
            "poblacion_meta": self.poblacion_meta,
            "modalidad_impl": self.modalidad_impl,
            "WA1_01_RM_NA": self.WA1_01_RM_NA,
            "WA1_01_RM_NN": self.WA1_01_RM_NN,
            "WA1_01_RM_MM": self.WA1_01_RM_MM,
            "WA1_01_RM_HH": self.WA1_01_RM_HH,
            "WA1_01_CA_NA": self.WA1_01_CA_NA,
            "WA1_01_CA_NN": self.WA1_01_CA_NN,
            "WA1_01_CA_MM": self.WA1_01_CA_MM,
            "WA1_01_CA_HH": self.WA1_01_CA_HH,
            "WA1_01_CUAL": self.WA1_01_CUAL
        }
        return to_return

    def __repr__(self):
        return self.__str__()

    def to_json(self):
        return self.__str__()


class SubFormWA1_02:
    def __init__(self,
                 mes,
                 colltmgkykvhxgij6,
                 rmrp,
                 covid,
                 wash_individual,
                 poblacion_meta,
                 WA1_02_RM_NA,
                 WA1_02_RM_NN,
                 WA1_02_RM_MM,
                 WA1_02_RM_HH,
                 WA1_02_CA_NA,
                 WA1_02_CA_NN,
                 WA1_02_CA_MM,
                 WA1_02_CA_HH,
                 WA1_02_CUAL
                 ):
        self.mes = mes
        self.colltmgkykvhxgij6 = colltmgkykvhxgij6
        self.rmrp = rmrp
        self.covid = covid
        self.wash_individual = wash_individual
        self.poblacion_meta = poblacion_meta
        self.WA1_02_RM_NA = WA1_02_RM_NA
        self.WA1_02_RM_NN = WA1_02_RM_NN
        self.WA1_02_RM_MM = WA1_02_RM_MM
        self.WA1_02_RM_HH = WA1_02_RM_HH
        self.WA1_02_CA_NA = WA1_02_CA_NA
        self.WA1_02_CA_NN = WA1_02_CA_NN
        self.WA1_02_CA_MM = WA1_02_CA_MM
        self.WA1_02_CA_HH = WA1_02_CA_HH
        self.WA1_02_CUAL = WA1_02_CUAL

    def __str__(self):
        to_return = {
            "mes": self.mes,
            "colltmgkykvhxgij6": self.colltmgkykvhxgij6,
            "rmrp": self.rmrp,
            "covid": self.covid,
            "wash_individual": self.wash_individual,
            "poblacion_meta": self.poblacion_meta,
            "WA1_02_RM_NA": self.WA1_02_RM_NA,
            "WA1_02_RM_NN": self.WA1_02_RM_NN,
            "WA1_02_RM_MM": self.WA1_02_RM_MM,
            "WA1_02_RM_HH": self.WA1_02_RM_HH,
            "WA1_02_CA_NA": self.WA1_02_CA_NA,
            "WA1_02_CA_NN": self.WA1_02_CA_NN,
            "WA1_02_CA_MM": self.WA1_02_CA_MM,
            "WA1_02_CA_HH": self.WA1_02_CA_HH,
            "WA1_02_CUAL": self.WA1_02_CUAL
        }
        return to_return

    def __repr__(self):
        return self.__str__()

    def to_json(self):
        return self.__str__()


class SubFormWA2_02:
    def __init__(self,
                 mes,
                 colltmgkykvhxgij6,
                 rmrp,
                 covid,
                 WA2_02,
                 WA2_02_CUAL
                 ):
        self.mes = mes
        self.colltmgkykvhxgij6 = colltmgkykvhxgij6
        self.rmrp = rmrp
        self.covid = covid
        self.WA2_02 = WA2_02
        self.WA2_02_CUAL = WA2_02_CUAL

    def __str__(self):
        to_return = {
            "mes": self.mes,
            "colltmgkykvhxgij6": self.colltmgkykvhxgij6,
            "rmrp": self.rmrp,
            "covid": self.covid,
            "WA2_02": self.WA2_02,
            "WA2_02_CUAL": self.WA2_02_CUAL
        }
        return to_return

    def __repr__(self):
        return self.__str__()

    def to_json(self):
        return self.__str__()


class SubFormIN4_01:
    def __init__(self,
                 mes,
                 colltmgkykvhxgij6,
                 rmrp,
                 poblacion_meta,
                 IN4_01_RM_NA,
                 IN4_01_RM_NN,
                 IN4_01_RM_MM,
                 IN4_01_RM_HH,
                 IN4_01_RM_OTR,
                 IN4_01_RM_LGBT,
                 IN4_01_CA_NA,
                 IN4_01_CA_NN,
                 IN4_01_CA_MM,
                 IN4_01_CA_HH,
                 IN4_01_CA_OTR,
                 IN4_01_CA_LGBT,
                 IN4_01_CUAL
                 ):
        self.mes = mes
        self.colltmgkykvhxgij6 = colltmgkykvhxgij6
        self.rmrp = rmrp
        self.poblacion_meta = poblacion_meta
        self.IN4_01_RM_NA = IN4_01_RM_NA
        self.IN4_01_RM_NN = IN4_01_RM_NN
        self.IN4_01_RM_MM = IN4_01_RM_MM
        self.IN4_01_RM_HH = IN4_01_RM_HH
        self.IN4_01_RM_OTR = IN4_01_RM_OTR
        self.IN4_01_RM_LGBT = IN4_01_RM_LGBT
        self.IN4_01_CA_NA = IN4_01_CA_NA
        self.IN4_01_CA_NN = IN4_01_CA_NN
        self.IN4_01_CA_MM = IN4_01_CA_MM
        self.IN4_01_CA_HH = IN4_01_CA_HH
        self.IN4_01_CA_OTR = IN4_01_CA_OTR
        self.IN4_01_CA_LGBT = IN4_01_CA_LGBT
        self.IN4_01_CUAL = IN4_01_CUAL

    def __str__(self):
        to_return = {
            "mes": self.mes,
            "colltmgkykvhxgij6": self.colltmgkykvhxgij6,
            "rmrp": self.rmrp,
            "poblacion_meta": self.poblacion_meta,
            "IN4_01_RM_NA": self.IN4_01_RM_NA,
            "IN4_01_RM_NN": self.IN4_01_RM_NN,
            "IN4_01_RM_MM": self.IN4_01_RM_MM,
            "IN4_01_RM_HH": self.IN4_01_RM_HH,
            "IN4_01_RM_OTR": self.IN4_01_RM_OTR,
            "IN4_01_RM_LGBT": self.IN4_01_RM_LGBT,
            "IN4_01_CA_NA": self.IN4_01_CA_NA,
            "IN4_01_CA_NN": self.IN4_01_CA_NN,
            "IN4_01_CA_MM": self.IN4_01_CA_MM,
            "IN4_01_CA_HH": self.IN4_01_CA_HH,
            "IN4_01_CA_OTR": self.IN4_01_CA_OTR,
            "IN4_01_CA_LGBT": self.IN4_01_CA_LGBT,
            "IN4_01_CUAL": self.IN4_01_CUAL
        }
        return to_return

    def __repr__(self):
        return self.__str__()

    def to_json(self):
        return self.__str__()

class SubFormIN5_01:
    def __init__(self,
                 mes,
                 colltmgkykvhxgij6,
                 rmrp,
                 poblacion_meta,
                 poblacion_meta_freq,
                 modalidad_impl,
                 mecanismos,
                 transferencia,
                 usd_transfer,
                 semilla,
                 IN5_01_RM_MM_N,
                 IN5_01_RM_HH_N,
                 IN5_01_RM_OTR_N,
                 IN5_01_RM_LGBT_N,
                 IN5_01_CA_MM_N,
                 IN5_01_CA_HH_N,
                 IN5_01_CA_OTR_N,
                 IN5_01_CA_LGBT_N,
                 IN5_01_CUAL
                 ):
        self.mes = mes
        self.colltmgkykvhxgij6 = colltmgkykvhxgij6
        self.rmrp = rmrp
        self.poblacion_meta = poblacion_meta
        self.poblacion_meta_freq = poblacion_meta_freq
        self.modalidad_impl = modalidad_impl
        self.mecanismos = mecanismos
        self.transferencia = transferencia
        self.usd_transfer = usd_transfer
        self.semilla = semilla
        self.IN5_01_RM_MM_N = IN5_01_RM_MM_N
        self.IN5_01_RM_HH_N = IN5_01_RM_HH_N
        self.IN5_01_RM_OTR_N = IN5_01_RM_OTR_N
        self.IN5_01_RM_LGBT_N = IN5_01_RM_LGBT_N
        self.IN5_01_CA_MM_N = IN5_01_CA_MM_N
        self.IN5_01_CA_HH_N = IN5_01_CA_HH_N
        self.IN5_01_CA_OTR_N = IN5_01_CA_OTR_N
        self.IN5_01_CA_LGBT_N = IN5_01_CA_LGBT_N
        self.IN5_01_CUAL = IN5_01_CUAL

    def __str__(self):
        to_return = {
            "mes": self.mes,
            "colltmgkykvhxgij6": self.colltmgkykvhxgij6,
            "rmrp": self.rmrp,
            "poblacion_meta": self.poblacion_meta,
            "poblacion_meta_freq": self.poblacion_meta_freq,
            "modalidad_impl": self.modalidad_impl,
            "mecanismos": self.mecanismos,
            "transferencia": self.transferencia,
            "usd_transfer": self.usd_transfer,
            "semilla": self.semilla,
            "IN5_01_RM_MM_N": self.IN5_01_RM_MM_N,
            "IN5_01_RM_HH_N": self.IN5_01_RM_HH_N,
            "IN5_01_RM_OTR_N": self.IN5_01_RM_OTR_N,
            "IN5_01_RM_LGBT_N": self.IN5_01_RM_LGBT_N,
            "IN5_01_CA_MM_N": self.IN5_01_CA_MM_N,
            "IN5_01_CA_HH_N": self.IN5_01_CA_HH_N,
            "IN5_01_CA_OTR_N": self.IN5_01_CA_OTR_N,
            "IN5_01_CA_LGBT_N": self.IN5_01_CA_LGBT_N,
            "IN5_01_CUAL": self.IN5_01_CUAL
        }
        return to_return

    def __repr__(self):
        return self.__str__()

    def to_json(self):
        return self.__str__()

class SubFormIN5_01_O89:
    def __init__(self,
                 mes,
                 colltmgkykvhxgij6,
                 rmrp,
                 covid,
                 espacion_apoyo,
                 poblacion_meta,
                 poblacion_meta_freq,
                 modalidad_impl,
                 mecanismos,
                 transferencia,
                 usd_transfer,
                 semilla,
                 IN5_01_RM_MM_N,
                 IN5_01_RM_HH_N,
                 IN5_01_RM_OTR_N,
                 IN5_01_RM_LGBT_N,
                 IN5_01_CA_MM_N,
                 IN5_01_CA_HH_N,
                 IN5_01_CA_OTR_N,
                 IN5_01_CA_LGBT_N,
                 IN5_01_CUAL
                 ):
        self.mes = mes
        self.colltmgkykvhxgij6 = colltmgkykvhxgij6
        self.rmrp = rmrp
        self.covid = covid
        self.espacion_apoyo = espacion_apoyo
        self.poblacion_meta = poblacion_meta
        self.poblacion_meta_freq = poblacion_meta_freq
        self.modalidad_impl = modalidad_impl
        self.mecanismos = mecanismos
        self.transferencia = transferencia
        self.usd_transfer = usd_transfer
        self.semilla = semilla
        self.IN5_01_RM_MM_N = IN5_01_RM_MM_N
        self.IN5_01_RM_HH_N = IN5_01_RM_HH_N
        self.IN5_01_RM_OTR_N = IN5_01_RM_OTR_N
        self.IN5_01_RM_LGBT_N = IN5_01_RM_LGBT_N
        self.IN5_01_CA_MM_N = IN5_01_CA_MM_N
        self.IN5_01_CA_HH_N = IN5_01_CA_HH_N
        self.IN5_01_CA_OTR_N = IN5_01_CA_OTR_N
        self.IN5_01_CA_LGBT_N = IN5_01_CA_LGBT_N
        self.IN5_01_CUAL = IN5_01_CUAL

    def __str__(self):
        to_return = {
            "mes": self.mes,
            "colltmgkykvhxgij6": self.colltmgkykvhxgij6,
            "rmrp": self.rmrp,
            "covid": self.covid,
            "espacion_apoyo": self.espacion_apoyo,
            "poblacion_meta": self.poblacion_meta,
            "poblacion_meta_freq": self.poblacion_meta_freq,
            "modalidad_impl": self.modalidad_impl,
            "mecanismos": self.mecanismos,
            "transferencia": self.transferencia,
            "usd_transfer": self.usd_transfer,
            "semilla": self.semilla,
            "IN5_01_RM_MM_N": self.IN5_01_RM_MM_N,
            "IN5_01_RM_HH_N": self.IN5_01_RM_HH_N,
            "IN5_01_RM_OTR_N": self.IN5_01_RM_OTR_N,
            "IN5_01_RM_LGBT_N": self.IN5_01_RM_LGBT_N,
            "IN5_01_CA_MM_N": self.IN5_01_CA_MM_N,
            "IN5_01_CA_HH_N": self.IN5_01_CA_HH_N,
            "IN5_01_CA_OTR_N": self.IN5_01_CA_OTR_N,
            "IN5_01_CA_LGBT_N": self.IN5_01_CA_LGBT_N,
            "IN5_01_CUAL": self.IN5_01_CUAL

        }
        return to_return

    def __repr__(self):
        return self.__str__()

    def to_json(self):
        return self.__str__()


class SubFormIN5_01_O88:
    def __init__(self,
                 mes,
                 colltmgkykvhxgij6,
                 rmrp,
                 covid,
                 espacion_apoyo,
                 poblacion_meta,
                 poblacion_meta_freq,
                 modalidad_impl,
                 mecanismos,
                 transferencia,
                 usd_transfer,
                 semilla,
                 IN5_01_RM_MM_R,
                 IN5_01_RM_HH_R,
                 IN5_01_RM_OTR_R,
                 IN5_01_RM_LGBT_R,
                 IN5_01_CA_MM_R,
                 IN5_01_CA_HH_R,
                 IN5_01_CA_OTR_R,
                 IN5_01_CA_LGBT_R,
                 IN5_01_CUAL
                 ):
        self.mes = mes
        self.colltmgkykvhxgij6 = colltmgkykvhxgij6
        self.rmrp = rmrp
        self.covid = covid
        self.espacion_apoyo = espacion_apoyo
        self.poblacion_meta = poblacion_meta
        self.poblacion_meta_freq = poblacion_meta_freq
        self.modalidad_impl = modalidad_impl
        self.mecanismos = mecanismos
        self.transferencia = transferencia
        self.usd_transfer = usd_transfer
        self.semilla = semilla
        self.IN5_01_RM_MM_R = IN5_01_RM_MM_R
        self.IN5_01_RM_HH_R = IN5_01_RM_HH_R
        self.IN5_01_RM_OTR_R = IN5_01_RM_OTR_R
        self.IN5_01_RM_LGBT_R = IN5_01_RM_LGBT_R
        self.IN5_01_CA_MM_R = IN5_01_CA_MM_R
        self.IN5_01_CA_HH_R = IN5_01_CA_HH_R
        self.IN5_01_CA_OTR_R = IN5_01_CA_OTR_R
        self.IN5_01_CA_LGBT_R = IN5_01_CA_LGBT_R
        self.IN5_01_CUAL = IN5_01_CUAL

    def __str__(self):
        to_return = {
            "mes": self.mes,
            "colltmgkykvhxgij6": self.colltmgkykvhxgij6,
            "rmrp": self.rmrp,
            "covid": self.covid,
            "espacion_apoyo": self.espacion_apoyo,
            "poblacion_meta": self.poblacion_meta,
            "poblacion_meta_freq": self.poblacion_meta_freq,
            "modalidad_impl": self.modalidad_impl,
            "mecanismos": self.mecanismos,
            "transferencia": self.transferencia,
            "usd_transfer": self.usd_transfer,
            "semilla": self.semilla,
            "IN5_01_RM_MM_R": self.IN5_01_RM_MM_R,
            "IN5_01_RM_HH_R": self.IN5_01_RM_HH_R,
            "IN5_01_RM_OTR_R": self.IN5_01_RM_OTR_R,
            "IN5_01_RM_LGBT_R": self.IN5_01_RM_LGBT_R,
            "IN5_01_CA_MM_R": self.IN5_01_CA_MM_R,
            "IN5_01_CA_HH_R": self.IN5_01_CA_HH_R,
            "IN5_01_CA_OTR_R": self.IN5_01_CA_OTR_R,
            "IN5_01_CA_LGBT_R": self.IN5_01_CA_LGBT_R,
            "IN5_01_CUAL": self.IN5_01_CUAL

        }
        return to_return

    def __repr__(self):
        return self.__str__()

    def to_json(self):
        return self.__str__()


class SubFormIN5_01_O52_63_66:
    def __init__(self,
                 mes,
                 colltmgkykvhxgij6,
                 rmrp,
                 covid,
                 espacion_apoyo,
                 poblacion_meta,
                 poblacion_meta_freq,
                 modalidad_impl,
                 semilla,
                 IN5_01_RM_MM_N,
                 IN5_01_RM_HH_N,
                 IN5_01_RM_OTR_N,
                 IN5_01_RM_LGBT_N,
                 IN5_01_CA_MM_N,
                 IN5_01_CA_HH_N,
                 IN5_01_CA_OTR_N,
                 IN5_01_CA_LGBT_N,
                 IN5_01_CUAL
                 ):
        self.mes = mes
        self.colltmgkykvhxgij6 = colltmgkykvhxgij6
        self.rmrp = rmrp
        self.covid = covid
        self.espacion_apoyo = espacion_apoyo
        self.poblacion_meta = poblacion_meta
        self.poblacion_meta_freq = poblacion_meta_freq
        self.modalidad_impl = modalidad_impl
        self.semilla = semilla
        self.IN5_01_RM_MM_N = IN5_01_RM_MM_N
        self.IN5_01_RM_HH_N = IN5_01_RM_HH_N
        self.IN5_01_RM_OTR_N = IN5_01_RM_OTR_N
        self.IN5_01_RM_LGBT_N = IN5_01_RM_LGBT_N
        self.IN5_01_CA_MM_N = IN5_01_CA_MM_N
        self.IN5_01_CA_HH_N = IN5_01_CA_HH_N
        self.IN5_01_CA_OTR_N = IN5_01_CA_OTR_N
        self.IN5_01_CA_LGBT_N = IN5_01_CA_LGBT_N
        self.IN5_01_CUAL = IN5_01_CUAL

    def __str__(self):
        to_return = {
            "mes": self.mes,
            "colltmgkykvhxgij6": self.colltmgkykvhxgij6,
            "rmrp": self.rmrp,
            "covid": self.covid,
            "espacion_apoyo": self.espacion_apoyo,
            "poblacion_meta": self.poblacion_meta,
            "poblacion_meta_freq": self.poblacion_meta_freq,
            "modalidad_impl": self.modalidad_impl,
            "semilla": self.semilla,
            "IN5_01_RM_MM_N": self.IN5_01_RM_MM_N,
            "IN5_01_RM_HH_N": self.IN5_01_RM_HH_N,
            "IN5_01_RM_OTR_N": self.IN5_01_RM_OTR_N,
            "IN5_01_RM_LGBT_N": self.IN5_01_RM_LGBT_N,
            "IN5_01_CA_MM_N": self.IN5_01_CA_MM_N,
            "IN5_01_CA_HH_N": self.IN5_01_CA_HH_N,
            "IN5_01_CA_OTR_N": self.IN5_01_CA_OTR_N,
            "IN5_01_CA_LGBT_N": self.IN5_01_CA_LGBT_N,
            "IN5_01_CUAL": self.IN5_01_CUAL

        }
        return to_return

    def __repr__(self):
        return self.__str__()

    def to_json(self):
        return self.__str__()


class SubFormAT1_01:
    def __init__(self,
                 mes,
                 colltmgkykvhxgij6,
                 rmrp,
                 poblacion_meta,
                 poblacion_meta_freq,
                 modalidad_impl,
                 AT1_01_RM_NA_PR,
                 AT1_01_RM_NN_PR,
                 AT1_01_RM_MM_PR,
                 AT1_01_RM_HH_PR,
                 AT1_01_RM_OTR_PR,
                 AT1_01_RM_LGBT_PR,
                 AT1_01_CA_NA_PR,
                 AT1_01_CA_NN_PR,
                 AT1_01_CA_MM_PR,
                 AT1_01_CA_HH_PR,
                 AT1_01_CA_OTR_PR,
                 AT1_01_CA_LGBT_PR
                 ):
        self.mes = mes
        self.colltmgkykvhxgij6 = colltmgkykvhxgij6
        self.rmrp = rmrp
        self.poblacion_meta = poblacion_meta
        self.poblacion_meta_freq = poblacion_meta_freq
        self.modalidad_impl = modalidad_impl
        self.AT1_01_RM_NA_PR = AT1_01_RM_NA_PR
        self.AT1_01_RM_NN_PR = AT1_01_RM_NN_PR
        self.AT1_01_RM_MM_PR = AT1_01_RM_MM_PR
        self.AT1_01_RM_HH_PR = AT1_01_RM_HH_PR
        self.AT1_01_RM_OTR_PR = AT1_01_RM_OTR_PR
        self.AT1_01_RM_LGBT_PR = AT1_01_RM_LGBT_PR
        self.AT1_01_CA_NA_PR = AT1_01_CA_NA_PR
        self.AT1_01_CA_NN_PR = AT1_01_CA_NN_PR
        self.AT1_01_CA_MM_PR = AT1_01_CA_MM_PR
        self.AT1_01_CA_HH_PR = AT1_01_CA_HH_PR
        self.AT1_01_CA_OTR_PR = AT1_01_CA_OTR_PR
        self.AT1_01_CA_LGBT_PR = AT1_01_CA_LGBT_PR

    def __str__(self):
        to_return = {
            "mes": self.mes,
            "colltmgkykvhxgij6": self.colltmgkykvhxgij6,
            "rmrp": self.rmrp,
            "poblacion_meta": self.poblacion_meta,
            "poblacion_meta_freq": self.poblacion_meta_freq,
            "modalidad_impl": self.modalidad_impl,
            "AT1_01_RM_NA_PR": self.AT1_01_RM_NA_PR,
            "AT1_01_RM_NN_PR": self.AT1_01_RM_NN_PR,
            "AT1_01_RM_MM_PR": self.AT1_01_RM_MM_PR,
            "AT1_01_RM_HH_PR": self.AT1_01_RM_HH_PR,
            "AT1_01_RM_OTR_PR": self.AT1_01_RM_OTR_PR,
            "AT1_01_RM_LGBT_PR": self.AT1_01_RM_LGBT_PR,
            "AT1_01_CA_NA_PR": self.AT1_01_CA_NA_PR,
            "AT1_01_CA_NN_PR": self.AT1_01_CA_NN_PR,
            "AT1_01_CA_MM_PR": self.AT1_01_CA_MM_PR,
            "AT1_01_CA_HH_PR": self.AT1_01_CA_HH_PR,
            "AT1_01_CA_OTR_PR": self.AT1_01_CA_OTR_PR,
            "AT1_01_CA_LGBT_PR": self.AT1_01_CA_LGBT_PR
        }
        return to_return

    def __repr__(self):
        return self.__str__()

    def to_json(self):
        return self.__str__()


class SubFormAT3_01:
    def __init__(self,
                 mes,
                 colltmgkykvhxgij6,
                 rmrp,
                 covid,
                 poblacion_meta,
                 modalidad_impl,
                 AT3_01_RM_NA,
                 AT3_01_RM_NN,
                 AT3_01_RM_MM,
                 AT3_01_RM_HH,
                 AT3_01_RM_OTR,
                 AT3_01_RM_LGBT,
                 AT3_01_CUAL
                 ):
        self.mes = mes
        self.colltmgkykvhxgij6 = colltmgkykvhxgij6
        self.rmrp = rmrp
        self.covid = covid
        self.poblacion_meta = poblacion_meta
        self.modalidad_impl = modalidad_impl
        self.AT3_01_RM_NA = AT3_01_RM_NA
        self.AT3_01_RM_NN = AT3_01_RM_NN
        self.AT3_01_RM_MM = AT3_01_RM_MM
        self.AT3_01_RM_HH = AT3_01_RM_HH
        self.AT3_01_RM_OTR = AT3_01_RM_OTR
        self.AT3_01_RM_LGBT = AT3_01_RM_LGBT
        self.AT3_01_CUAL = AT3_01_CUAL

    def __str__(self):
        to_return = {
            "mes": self.mes,
            "colltmgkykvhxgij6": self.colltmgkykvhxgij6,
            "rmrp": self.rmrp,
            "covid": self.covid,
            "poblacion_meta": self.poblacion_meta,
            "modalidad_impl": self.modalidad_impl,
            "AT3_01_RM_NA": self.AT3_01_RM_NA,
            "AT3_01_RM_NN": self.AT3_01_RM_NN,
            "AT3_01_RM_MM": self.AT3_01_RM_MM,
            "AT3_01_RM_HH": self.AT3_01_RM_HH,
            "AT3_01_RM_OTR": self.AT3_01_RM_OTR,
            "AT3_01_RM_LGBT": self.AT3_01_RM_LGBT,
            "AT3_01_CUAL": self.AT3_01_CUAL
        }
        return to_return

    def __repr__(self):
        return self.__str__()

    def to_json(self):
        return self.__str__()



class SubFormAT5_01:
    def __init__(self,
                 mes,
                 colltmgkykvhxgij6,
                 rmrp,
                 poblacion_meta,
                 modalidad_impl,
                 tipo_kit,
                 AT5_01_RM_NA_BB,
                 AT5_01_RM_NN_BB,
                 AT5_01_CA_NA_BB,
                 AT5_01_CA_NN_BB,
                 AT5_01_CUAL
                 ):
        self.mes = mes
        self.colltmgkykvhxgij6 = colltmgkykvhxgij6
        self.rmrp = rmrp
        self.poblacion_meta = poblacion_meta
        self.modalidad_impl = modalidad_impl
        self.tipo_kit = tipo_kit
        self.AT5_01_RM_NA_BB = AT5_01_RM_NA_BB
        self.AT5_01_RM_NN_BB = AT5_01_RM_NN_BB
        self.AT5_01_CA_NA_BB = AT5_01_CA_NA_BB
        self.AT5_01_CA_NN_BB = AT5_01_CA_NN_BB
        self.AT5_01_CUAL = AT5_01_CUAL

    def __str__(self):
        to_return = {
            "mes": self.mes,
            "colltmgkykvhxgij6": self.colltmgkykvhxgij6,
            "rmrp": self.rmrp,
            "poblacion_meta": self.poblacion_meta,
            "modalidad_impl": self.modalidad_impl,
            "tipo_kit": self.tipo_kit,
            "AT5_01_RM_NA_BB": self.AT5_01_RM_NA_BB,
            "AT5_01_RM_NN_BB": self.AT5_01_RM_NN_BB,
            "AT5_01_CA_NA_BB": self.AT5_01_CA_NA_BB,
            "AT5_01_CA_NN_BB": self.AT5_01_CA_NN_BB,
            "AT5_01_CUAL": self.AT5_01_CUAL
        }
        return to_return

    def __repr__(self):
        return self.__str__()

    def to_json(self):
        return self.__str__()


class SubFormAT1_01:
    def __init__(self,
                 mes,
                 colltmgkykvhxgij6,
                 rmrp,
                 poblacion_meta,
                 poblacion_meta_freq,
                 modalidad_impl,
                 AT1_01_RM_NA_PR,
                 AT1_01_RM_NN_PR,
                 AT1_01_RM_MM_PR,
                 AT1_01_RM_HH_PR,
                 AT1_01_RM_OTR_PR,
                 AT1_01_RM_LGBT_PR,
                 AT1_01_CA_NA_PR,
                 AT1_01_CA_NN_PR,
                 AT1_01_CA_MM_PR,
                 AT1_01_CA_HH_PR,
                 AT1_01_CA_OTR_PR,
                 AT1_01_CA_LGBT_PR
                 ):
        self.mes = mes
        self.colltmgkykvhxgij6 = colltmgkykvhxgij6
        self.rmrp = rmrp
        self.poblacion_meta = poblacion_meta
        self.poblacion_meta_freq = poblacion_meta_freq
        self.modalidad_impl = modalidad_impl
        self.AT1_01_RM_NA_PR = AT1_01_RM_NA_PR
        self.AT1_01_RM_NN_PR = AT1_01_RM_NN_PR
        self.AT1_01_RM_MM_PR = AT1_01_RM_MM_PR
        self.AT1_01_RM_HH_PR = AT1_01_RM_HH_PR
        self.AT1_01_RM_OTR_PR = AT1_01_RM_OTR_PR
        self.AT1_01_RM_LGBT_PR = AT1_01_RM_LGBT_PR
        self.AT1_01_CA_NA_PR = AT1_01_CA_NA_PR
        self.AT1_01_CA_NN_PR = AT1_01_CA_NN_PR
        self.AT1_01_CA_MM_PR = AT1_01_CA_MM_PR
        self.AT1_01_CA_HH_PR = AT1_01_CA_HH_PR
        self.AT1_01_CA_OTR_PR = AT1_01_CA_OTR_PR
        self.AT1_01_CA_LGBT_PR = AT1_01_CA_LGBT_PR

    def __str__(self):
        to_return = {
            "mes": self.mes,
            "colltmgkykvhxgij6": self.colltmgkykvhxgij6,
            "rmrp": self.rmrp,
            "poblacion_meta": self.poblacion_meta,
            "poblacion_meta_freq": self.poblacion_meta_freq,
            "modalidad_impl": self.modalidad_impl,
            "AT1_01_RM_NA_PR": self.AT1_01_RM_NA_PR,
            "AT1_01_RM_NN_PR": self.AT1_01_RM_NN_PR,
            "AT1_01_RM_MM_PR": self.AT1_01_RM_MM_PR,
            "AT1_01_RM_HH_PR": self.AT1_01_RM_HH_PR,
            "AT1_01_RM_OTR_PR": self.AT1_01_RM_OTR_PR,
            "AT1_01_RM_LGBT_PR": self.AT1_01_RM_LGBT_PR,
            "AT1_01_CA_NA_PR": self.AT1_01_CA_NA_PR,
            "AT1_01_CA_NN_PR": self.AT1_01_CA_NN_PR,
            "AT1_01_CA_MM_PR": self.AT1_01_CA_MM_PR,
            "AT1_01_CA_HH_PR": self.AT1_01_CA_HH_PR,
            "AT1_01_CA_OTR_PR": self.AT1_01_CA_OTR_PR,
            "AT1_01_CA_LGBT_PR": self.AT1_01_CA_LGBT_PR
        }
        return to_return

    def __repr__(self):
        return self.__str__()

    def to_json(self):
        return self.__str__()


class SubFormAT1_04:
    def __init__(self,
                 mes,
                 colltmgkykvhxgij6,
                 rmrp,
                 AT1_04,
                 AT1_04_CUAL
                 ):
        self.mes = mes
        self.colltmgkykvhxgij6 = colltmgkykvhxgij6
        self.rmrp = rmrp
        self.AT1_04 = AT1_04
        self.AT1_04_CUAL = AT1_04_CUAL

    def __str__(self):
        to_return = {
            "mes": self.mes,
            "colltmgkykvhxgij6": self.colltmgkykvhxgij6,
            "rmrp": self.rmrp,
            "AT1_04": self.AT1_04,
            "AT1_04_CUAL": self.AT1_04_CUAL
        }
        return to_return

    def __repr__(self):
        return self.__str__()

    def to_json(self):
        return self.__str__()


class SubFormAT3_01:
    def __init__(self,
                 mes,
                 colltmgkykvhxgij6,
                 rmrp,
                 covid,
                 poblacion_meta,
                 modalidad_impl,
                 AT3_01_RM_NA,
                 AT3_01_RM_NN,
                 AT3_01_RM_MM,
                 AT3_01_RM_HH,
                 AT3_01_RM_OTR,
                 AT3_01_RM_LGBT,
                 AT3_01_CUAL
                 ):
        self.mes = mes
        self.colltmgkykvhxgij6 = colltmgkykvhxgij6
        self.rmrp = rmrp
        self.covid = covid
        self.poblacion_meta = poblacion_meta
        self.modalidad_impl = modalidad_impl
        self.AT3_01_RM_NA = AT3_01_RM_NA
        self.AT3_01_RM_NN = AT3_01_RM_NN
        self.AT3_01_RM_MM = AT3_01_RM_MM
        self.AT3_01_RM_HH = AT3_01_RM_HH
        self.AT3_01_RM_OTR = AT3_01_RM_OTR
        self.AT3_01_RM_LGBT = AT3_01_RM_LGBT
        self.AT3_01_CUAL = AT3_01_CUAL

    def __str__(self):
        to_return = {
            "mes": self.mes,
            "colltmgkykvhxgij6": self.colltmgkykvhxgij6,
            "rmrp": self.rmrp,
            "covid": self.covid,
            "poblacion_meta": self.poblacion_meta,
            "modalidad_impl": self.modalidad_impl,
            "AT3_01_RM_NA": self.AT3_01_RM_NA,
            "AT3_01_RM_NN": self.AT3_01_RM_NN,
            "AT3_01_RM_MM": self.AT3_01_RM_MM,
            "AT3_01_RM_HH": self.AT3_01_RM_HH,
            "AT3_01_RM_OTR": self.AT3_01_RM_OTR,
            "AT3_01_RM_LGBT": self.AT3_01_RM_LGBT,
            "AT3_01_CUAL": self.AT3_01_CUAL
        }
        return to_return

    def __repr__(self):
        return self.__str__()

    def to_json(self):
        return self.__str__()


class SubFormAT4_01:
    def __init__(self,
                 mes,
                 colltmgkykvhxgij6,
                 rmrp,
                 AT4_01,
                 AT4_01_CUAL
                 ):
        self.mes = mes
        self.colltmgkykvhxgij6 = colltmgkykvhxgij6
        self.rmrp = rmrp
        self.AT4_01 = AT4_01
        self.AT4_01_CUAL = AT4_01_CUAL

    def __str__(self):
        to_return = {
            "mes": self.mes,
            "colltmgkykvhxgij6": self.colltmgkykvhxgij6,
            "rmrp": self.rmrp,
            "AT4_01": self.AT4_01,
            "AT4_01_CUAL": self.AT4_01_CUAL
        }
        return to_return

    def __repr__(self):
        return self.__str__()

    def to_json(self):
        return self.__str__()

class SubFormACBI1_01:
    def __init__(self,
                 mes,
                 colltmgkykvhxgij6,
                 rmrp,
                 poblacion_meta,
                 modalidad_impl,
                 mecanismos,
                 transferencia,
                 usd_transfer,
                 CBI1_01_RM_NA_U,
                 CBI1_01_RM_NN_U,
                 CBI1_01_RM_MM_U,
                 CBI1_01_RM_HH_U,
                 CBI1_01_CA_NA_U,
                 CBI1_01_CA_NN_U,
                 CBI1_01_CA_MM_U,
                 CBI1_01_CA_HH_U,
                 CBI1_01_CUAL
                 ):
        self.mes = mes
        self.colltmgkykvhxgij6 = colltmgkykvhxgij6
        self.rmrp = rmrp
        self.poblacion_meta = poblacion_meta
        self.modalidad_impl = modalidad_impl
        self.mecanismos = mecanismos
        self.transferencia = transferencia
        self.usd_transfer = usd_transfer
        self.CBI1_01_RM_NA_U = CBI1_01_RM_NA_U
        self.CBI1_01_RM_NN_U = CBI1_01_RM_NN_U
        self.CBI1_01_RM_MM_U = CBI1_01_RM_MM_U
        self.CBI1_01_RM_HH_U = CBI1_01_RM_HH_U
        self.CBI1_01_CA_NA_U = CBI1_01_CA_NA_U
        self.CBI1_01_CA_NN_U = CBI1_01_CA_NN_U
        self.CBI1_01_CA_MM_U = CBI1_01_CA_MM_U
        self.CBI1_01_CA_HH_U = CBI1_01_CA_HH_U
        self.CBI1_01_CUAL = CBI1_01_CUAL

    def __str__(self):
        to_return = {
            "mes": self.mes,
            "colltmgkykvhxgij6": self.colltmgkykvhxgij6,
            "rmrp": self.rmrp,
            "poblacion_meta": self.poblacion_meta,
            "modalidad_impl": self.modalidad_impl,
            "mecanismos": self.mecanismos,
            "transferencia": self.transferencia,
            "usd_transfer": self.usd_transfer,
            "CBI1_01_RM_NA_U": self.CBI1_01_RM_NA_U,
            "CBI1_01_RM_NN_U": self.CBI1_01_RM_NN_U,
            "CBI1_01_RM_MM_U": self.CBI1_01_RM_MM_U,
            "CBI1_01_RM_HH_U": self.CBI1_01_RM_HH_U,
            "CBI1_01_CA_NA_U": self.CBI1_01_CA_NA_U,
            "CBI1_01_CA_NN_U": self.CBI1_01_CA_NN_U,
            "CBI1_01_CA_MM_U": self.CBI1_01_CA_MM_U,
            "CBI1_01_CA_HH_U": self.CBI1_01_CA_HH_U,
            "CBI1_01_CUAL": self.CBI1_01_CUAL
        }
        return to_return

    def __repr__(self):
        return self.__str__()

    def to_json(self):
        return self.__str__()


class SubFormACBI2_01:
    def __init__(self,
                 mes,
                 colltmgkykvhxgij6,
                 rmrp,
                 covid,
                 espacion_apoyo,
                 poblacion_meta,
                 poblacion_meta_freq,
                 modalidad_impl,
                 mecanismos,
                 transferencia,
                 usd_transfer,
                 CBI2_01_RM_NA_PR,
                 CBI2_01_RM_NN_PR,
                 CBI2_01_RM_MM_PR,
                 CBI2_01_RM_HH_PR,
                 CBI2_01_CA_NA_PR,
                 CBI2_01_CA_NN_PR,
                 CBI2_01_CA_MM_PR,
                 CBI2_01_CA_HH_PR,
                 CBI2_01_CUAL
                 ):
        self.mes = mes
        self.colltmgkykvhxgij6 = colltmgkykvhxgij6
        self.rmrp = rmrp
        self.covid = covid
        self.espacion_apoyo = espacion_apoyo
        self.poblacion_meta = poblacion_meta
        self.poblacion_meta_freq = poblacion_meta_freq
        self.modalidad_impl = modalidad_impl
        self.mecanismos = mecanismos
        self.transferencia = transferencia
        self.usd_transfer = usd_transfer
        self.CBI2_01_RM_NA_PR = CBI2_01_RM_NA_PR
        self.CBI2_01_RM_NN_PR = CBI2_01_RM_NN_PR
        self.CBI2_01_RM_MM_PR = CBI2_01_RM_MM_PR
        self.CBI2_01_RM_HH_PR = CBI2_01_RM_HH_PR
        self.CBI2_01_CA_NA_PR = CBI2_01_CA_NA_PR
        self.CBI2_01_CA_NN_PR = CBI2_01_CA_NN_PR
        self.CBI2_01_CA_MM_PR = CBI2_01_CA_MM_PR
        self.CBI2_01_CA_HH_PR = CBI2_01_CA_HH_PR
        self.CBI2_01_CUAL = CBI2_01_CUAL

    def __str__(self):
        to_return = {
            "mes": self.mes,
            "colltmgkykvhxgij6": self.colltmgkykvhxgij6,
            "rmrp": self.rmrp,
            "covid": self.covid,
            "espacion_apoyo": self.espacion_apoyo,
            "poblacion_meta": self.poblacion_meta,
            "poblacion_meta_freq": self.poblacion_meta_freq,
            "modalidad_impl": self.modalidad_impl,
            "mecanismos": self.mecanismos,
            "transferencia": self.transferencia,
            "usd_transfer": self.usd_transfer,
            "CBI2_01_RM_NA_PR": self.CBI2_01_RM_NA_PR,
            "CBI2_01_RM_NN_PR": self.CBI2_01_RM_NN_PR,
            "CBI2_01_RM_MM_PR": self.CBI2_01_RM_MM_PR,
            "CBI2_01_RM_HH_PR": self.CBI2_01_RM_HH_PR,
            "CBI2_01_CA_NA_PR": self.CBI2_01_CA_NA_PR,
            "CBI2_01_CA_NN_PR": self.CBI2_01_CA_NN_PR,
            "CBI2_01_CA_MM_PR": self.CBI2_01_CA_MM_PR,
            "CBI2_01_CA_HH_PR": self.CBI2_01_CA_HH_PR,
            "CBI2_01_CUAL": self.CBI2_01_CUAL
        }
        return to_return

    def __repr__(self):
        return self.__str__()

    def to_json(self):
        return self.__str__()

class SubFormCO1_01:
    def __init__(self,
                 mes,
                 colltmgkykvhxgij6,
                 rmrp,
                 sector,
                 CO1_01,
                 CO1_01_CUAL

                 ):
        self.mes = mes
        self.colltmgkykvhxgij6 = colltmgkykvhxgij6
        self.rmrp = rmrp
        self.sector = sector
        self.CO1_01 = CO1_01
        self.CO1_01_CUAL = CO1_01_CUAL

    def __str__(self):
        to_return = {
            "mes": self.mes,
            "colltmgkykvhxgij6": self.colltmgkykvhxgij6,
            "rmrp": self.rmrp,
            "sector": self.sector,
            "CO1_01": self.CO1_01,
            "CO1_01_CUAL": self.CO1_01_CUAL
        }
        return to_return

    def __repr__(self):
        return self.__str__()

    def to_json(self):
        return self.__str__()


class SubFormCO2_01:
    def __init__(self,
                 mes,
                 colltmgkykvhxgij6,
                 rmrp,
                 poblacion_meta2,
                 modalidad_impl,
                 CO2_01_RM_NA,
                 CO2_01_RM_NN,
                 CO2_01_RM_MM,
                 CO2_01_RM_HH,
                 CO2_01_RM_OTR,
                 CO2_01_RM_LGBT,
                 CO2_01_CUAL
                 ):
        self.mes = mes
        self.colltmgkykvhxgij6 = colltmgkykvhxgij6
        self.rmrp = rmrp
        self.poblacion_meta2 = poblacion_meta2
        self.modalidad_impl = modalidad_impl
        self.CO2_01_RM_NA = CO2_01_RM_NA
        self.CO2_01_RM_NN = CO2_01_RM_NN
        self.CO2_01_RM_MM = CO2_01_RM_MM
        self.CO2_01_RM_HH = CO2_01_RM_HH
        self.CO2_01_RM_OTR = CO2_01_RM_OTR
        self.CO2_01_RM_LGBT = CO2_01_RM_LGBT
        self.CO2_01_CUAL = CO2_01_CUAL

    def __str__(self):
        to_return = {
            "mes": self.mes,
            "colltmgkykvhxgij6": self.colltmgkykvhxgij6,
            "rmrp": self.rmrp,
            "poblacion_meta2": self.poblacion_meta2,
            "modalidad_impl": self.modalidad_impl,
            "CO2_01_RM_NA": self.CO2_01_RM_NA,
            "CO2_01_RM_NN": self.CO2_01_RM_NN,
            "CO2_01_RM_MM": self.CO2_01_RM_MM,
            "CO2_01_RM_HH": self.CO2_01_RM_HH,
            "CO2_01_RM_OTR": self.CO2_01_RM_OTR,
            "CO2_01_RM_LGBT": self.CO2_01_RM_LGBT,
            "CO2_01_CUAL": self.CO2_01_CUAL
        }
        return to_return

    def __repr__(self):
        return self.__str__()

    def to_json(self):
        return self.__str__()


class SubFormED2_01:
    def __init__(self,
                 mes,
                 colltmgkykvhxgij6,
                 rmrp,
                 poblacion_meta,
                 modalidad_impl,
                 mecanismos,
                 transferencia,
                 usd_transfer,
                 ED2_01_RM_NA,
                 ED2_01_RM_NN,
                 ED2_01_RM_OTR,
                 ED2_01_RM_ADM,
                 ED2_01_RM_ADH,
                 ED2_01_RM_ADOTR,
                 ED2_01_RM_MM,
                 ED2_01_RM_HH,
                 ED2_01_RM_DS,
                 ED2_01_CA_NA,
                 ED2_01_CA_NN,
                 ED2_01_CA_OTR,
                 ED2_01_CA_ADM,
                 ED2_01_CA_ADH,
                 ED2_01_CA_ADOTR,
                 ED2_01_CA_MM,
                 ED2_01_CA_HH,
                 ED2_01_CA_DS,
                 ED2_01_CUAL

                 ):
        self.mes = mes
        self.colltmgkykvhxgij6 = colltmgkykvhxgij6
        self.rmrp = rmrp
        self.poblacion_meta = poblacion_meta
        self.modalidad_impl = modalidad_impl
        self.mecanismos = mecanismos
        self.transferencia = transferencia
        self.usd_transfer = usd_transfer
        self.ED2_01_RM_NA = ED2_01_RM_NA
        self.ED2_01_RM_NN = ED2_01_RM_NN
        self.ED2_01_RM_OTR = ED2_01_RM_OTR
        self.ED2_01_RM_ADM = ED2_01_RM_ADM
        self.ED2_01_RM_ADH = ED2_01_RM_ADH
        self.ED2_01_RM_ADOTR = ED2_01_RM_ADOTR
        self.ED2_01_RM_MM = ED2_01_RM_MM
        self.ED2_01_RM_HH = ED2_01_RM_HH
        self.ED2_01_RM_DS = ED2_01_RM_DS
        self.ED2_01_CA_NA = ED2_01_CA_NA
        self.ED2_01_CA_NN = ED2_01_CA_NN
        self.ED2_01_CA_OTR = ED2_01_CA_OTR
        self.ED2_01_CA_ADM = ED2_01_CA_ADM
        self.ED2_01_CA_ADH = ED2_01_CA_ADH
        self.ED2_01_CA_ADOTR = ED2_01_CA_ADOTR
        self.ED2_01_CA_MM = ED2_01_CA_MM
        self.ED2_01_CA_HH = ED2_01_CA_HH
        self.ED2_01_CA_DS = ED2_01_CA_DS
        self.ED2_01_CUAL = ED2_01_CUAL

    def __str__(self):
        to_return = {
            "mes": self.mes,
            "colltmgkykvhxgij6": self.colltmgkykvhxgij6,
            "rmrp": self.rmrp,
            "poblacion_meta": self.poblacion_meta,
            "modalidad_impl": self.modalidad_impl,
            "mecanismos": self.mecanismos,
            "transferencia": self.transferencia,
            "usd_transfer": self.usd_transfer,
            "ED2_01_RM_NA": self.ED2_01_RM_NA,
            "ED2_01_RM_NN": self.ED2_01_RM_NN,
            "ED2_01_RM_OTR": self.ED2_01_RM_OTR,
            "ED2_01_RM_ADM": self.ED2_01_RM_ADM,
            "ED2_01_RM_ADH": self.ED2_01_RM_ADH,
            "ED2_01_RM_ADOTR": self.ED2_01_RM_ADOTR,
            "ED2_01_RM_MM": self.ED2_01_RM_MM,
            "ED2_01_RM_HH": self.ED2_01_RM_HH,
            "ED2_01_RM_DS": self.ED2_01_RM_DS,
            "ED2_01_CA_NA": self.ED2_01_CA_NA,
            "ED2_01_CA_NN": self.ED2_01_CA_NN,
            "ED2_01_CA_OTR": self.ED2_01_CA_OTR,
            "ED2_01_CA_ADM": self.ED2_01_CA_ADM,
            "ED2_01_CA_ADH": self.ED2_01_CA_ADH,
            "ED2_01_CA_ADOTR": self.ED2_01_CA_ADOTR,
            "ED2_01_CA_MM": self.ED2_01_CA_MM,
            "ED2_01_CA_HH": self.ED2_01_CA_HH,
            "ED2_01_CA_DS": self.ED2_01_CA_DS,
            "ED2_01_CUAL": self.ED2_01_CUAL
        }
        return to_return

    def __repr__(self):
        return self.__str__()

    def to_json(self):
        return self.__str__()



class SubFormED4_03:
    def __init__(self,
                 mes,
                 colltmgkykvhxgij6,
                 rmrp,
                 ED4_03,
                 ED4_03_CUAL

                 ):
        self.mes = mes
        self.colltmgkykvhxgij6 = colltmgkykvhxgij6
        self.rmrp = rmrp
        self.ED4_03 = ED4_03
        self.ED4_03_CUAL = ED4_03_CUAL

    def __str__(self):
        to_return = {
            "mes": self.mes,
            "colltmgkykvhxgij6": self.colltmgkykvhxgij6,
            "rmrp": self.rmrp,
            "ED4_03": self.ED4_03,
            "ED4_03_CUAL": self.ED4_03_CUAL
        }
        return to_return

    def __repr__(self):
        return self.__str__()

    def to_json(self):
        return self.__str__()


class SubFormED5_01:
    def __init__(self,
                 mes,
                 colltmgkykvhxgij6,
                 rmrp,
                 covid,
                 poblacion_meta,
                 modalidad_impl,
                 ED5_01_RM_NA,
                 ED5_01_RM_NN,
                 ED5_01_RM_DS,
                 ED5_01_CA_NA,
                 ED5_01_CA_NN,
                 ED5_01_CA_DS,
                 ED5_01_CUAL

                 ):
        self.mes = mes
        self.colltmgkykvhxgij6 = colltmgkykvhxgij6
        self.rmrp = rmrp
        self.covid = covid
        self.poblacion_meta = poblacion_meta
        self.modalidad_impl = modalidad_impl
        self.ED5_01_RM_NA = ED5_01_RM_NA
        self.ED5_01_RM_NN = ED5_01_RM_NN
        self.ED5_01_RM_DS = ED5_01_RM_DS
        self.ED5_01_CA_NA = ED5_01_CA_NA
        self.ED5_01_CA_NN = ED5_01_CA_NN
        self.ED5_01_CA_DS = ED5_01_CA_DS
        self.ED5_01_CUAL = ED5_01_CUAL

    def __str__(self):
        to_return = {
            "mes": self.mes,
            "colltmgkykvhxgij6": self.colltmgkykvhxgij6,
            "rmrp": self.rmrp,
            "covid": self.covid,
            "poblacion_meta": self.poblacion_meta,
            "modalidad_impl": self.modalidad_impl,
            "ED5_01_RM_NA": self.ED5_01_RM_NA,
            "ED5_01_RM_NN": self.ED5_01_RM_NN,
            "ED5_01_RM_DS": self.ED5_01_RM_DS,
            "ED5_01_CA_NA": self.ED5_01_CA_NA,
            "ED5_01_CA_NN": self.ED5_01_CA_NN,
            "ED5_01_CA_DS": self.ED5_01_CA_DS,
            "ED5_01_CUAL": self.ED5_01_CUAL
        }
        return to_return

    def __repr__(self):
        return self.__str__()

    def to_json(self):
        return self.__str__()


class SubFormED6_01:
    def __init__(self,
                 mes,
                 colltmgkykvhxgij6,
                 rmrp,
                 covid,
                 poblacion_meta,
                 modalidad_impl,
                 ED6_01_RM_NA,
                 ED6_01_RM_NN,
                 ED6_01_RM_MM,
                 ED6_01_RM_HH,
                 ED6_01_RM_DS,
                 ED6_01_CA_NA,
                 ED6_01_CA_NN,
                 ED6_01_CA_MM,
                 ED6_01_CA_HH,
                 ED6_01_CA_DS,
                 ED6_01_CUAL

                 ):
        self.mes = mes
        self.colltmgkykvhxgij6 = colltmgkykvhxgij6
        self.rmrp = rmrp
        self.covid = covid
        self.poblacion_meta = poblacion_meta
        self.modalidad_impl = modalidad_impl
        self.ED6_01_RM_NA = ED6_01_RM_NA
        self.ED6_01_RM_NN = ED6_01_RM_NN
        self.ED6_01_RM_MM = ED6_01_RM_MM
        self.ED6_01_RM_HH = ED6_01_RM_HH
        self.ED6_01_RM_DS = ED6_01_RM_DS
        self.ED6_01_CA_NA = ED6_01_CA_NA
        self.ED6_01_CA_NN = ED6_01_CA_NN
        self.ED6_01_CA_MM = ED6_01_CA_MM
        self.ED6_01_CA_HH = ED6_01_CA_HH
        self.ED6_01_CA_DS = ED6_01_CA_DS
        self.ED6_01_CUAL = ED6_01_CUAL

    def __str__(self):
        to_return = {
            "mes": self.mes,
            "colltmgkykvhxgij6": self.colltmgkykvhxgij6,
            "rmrp": self.rmrp,
            "covid": self.covid,
            "poblacion_meta": self.poblacion_meta,
            "modalidad_impl": self.modalidad_impl,
            "ED6_01_RM_NA": self.ED6_01_RM_NA,
            "ED6_01_RM_NN": self.ED6_01_RM_NN,
            "ED6_01_RM_MM": self.ED6_01_RM_MM,
            "ED6_01_RM_HH": self.ED6_01_RM_HH,
            "ED6_01_RM_DS": self.ED6_01_RM_DS,
            "ED6_01_CA_NA": self.ED6_01_CA_NA,
            "ED6_01_CA_NN": self.ED6_01_CA_NN,
            "ED6_01_CA_MM": self.ED6_01_CA_MM,
            "ED6_01_CA_HH": self.ED6_01_CA_HH,
            "ED6_01_CA_DS": self.ED6_01_CA_DS,
            "ED6_01_CUAL": self.ED6_01_CUAL
        }
        return to_return

    def __repr__(self):
        return self.__str__()

    def to_json(self):
        return self.__str__()


class SubFormIN1_01:
    def __init__(self,
                 mes,
                 colltmgkykvhxgij6,
                 rmrp,
                 poblacion_meta,
                 poblacion_meta_freq,
                 modalidad_impl,
                 mecanismos,
                 transferencia,
                 usd_transfer,
                 IN1_01_RM_MM_N,
                 IN1_01_RM_HH_N,
                 IN1_01_RM_OTR_N,
                 IN1_01_RM_LGBT_N,
                 IN1_01_CA_MM_N,
                 IN1_01_CA_HH_N,
                 IN1_01_CA_OTR_N,
                 IN1_01_CA_LGBT_N,
                 IN1_01_CUAL

                 ):
        self.mes = mes
        self.colltmgkykvhxgij6 = colltmgkykvhxgij6
        self.rmrp = rmrp
        self.poblacion_meta = poblacion_meta
        self.poblacion_meta_freq = poblacion_meta_freq
        self.modalidad_impl = modalidad_impl
        self.mecanismos = mecanismos
        self.transferencia = transferencia
        self.usd_transfer = usd_transfer
        self.IN1_01_RM_MM_N = IN1_01_RM_MM_N
        self.IN1_01_RM_HH_N = IN1_01_RM_HH_N
        self.IN1_01_RM_OTR_N = IN1_01_RM_OTR_N
        self.IN1_01_RM_LGBT_N = IN1_01_RM_LGBT_N
        self.IN1_01_CA_MM_N = IN1_01_CA_MM_N
        self.IN1_01_CA_HH_N = IN1_01_CA_HH_N
        self.IN1_01_CA_OTR_N = IN1_01_CA_OTR_N
        self.IN1_01_CA_LGBT_N = IN1_01_CA_LGBT_N
        self.IN1_01_CUAL = IN1_01_CUAL


    def __str__(self):
        to_return = {
            "mes": self.mes,
            "colltmgkykvhxgij6": self.colltmgkykvhxgij6,
            "rmrp": self.rmrp,
            "poblacion_meta": self.poblacion_meta,
            "poblacion_meta_freq": self.poblacion_meta_freq,
            "modalidad_impl": self.modalidad_impl,
            "mecanismos": self.mecanismos,
            "transferencia": self.transferencia,
            "usd_transfer": self.usd_transfer,
            "IN1_01_RM_MM_N": self.IN1_01_RM_MM_N,
            "IN1_01_RM_HH_N": self.IN1_01_RM_HH_N,
            "IN1_01_RM_OTR_N": self.IN1_01_RM_OTR_N,
            "IN1_01_RM_LGBT_N": self.IN1_01_RM_LGBT_N,
            "IN1_01_CA_MM_N": self.IN1_01_CA_MM_N,
            "IN1_01_CA_HH_N": self.IN1_01_CA_HH_N,
            "IN1_01_CA_OTR_N": self.IN1_01_CA_OTR_N,
            "IN1_01_CA_LGBT_N": self.IN1_01_CA_LGBT_N,
            "IN1_01_CUAL": self.IN1_01_CUAL
        }
        return to_return

    def __repr__(self):
        return self.__str__()

    def to_json(self):
        return self.__str__()


class SubFormIN2_01:
    def __init__(self,
                 mes,
                 colltmgkykvhxgij6,
                 rmrp,
                 poblacion_meta,
                 modalidad_impl,
                 IN2_01_RM_MM,
                 IN2_01_RM_HH,
                 IN2_01_RM_OTR,
                 IN2_01_CA_MM,
                 IN2_01_CA_HH,
                 IN2_01_CA_OTR,
                 IN2_01_CUAL
                 ):
        self.mes = mes
        self.colltmgkykvhxgij6 = colltmgkykvhxgij6
        self.rmrp = rmrp
        self.poblacion_meta = poblacion_meta
        self.modalidad_impl = modalidad_impl
        self.IN2_01_RM_MM = IN2_01_RM_MM
        self.IN2_01_RM_HH = IN2_01_RM_HH
        self.IN2_01_RM_OTR = IN2_01_RM_OTR
        self.IN2_01_CA_MM = IN2_01_CA_MM
        self.IN2_01_CA_HH = IN2_01_CA_HH
        self.IN2_01_CA_OTR = IN2_01_CA_OTR
        self.IN2_01_CUAL = IN2_01_CUAL

    def __str__(self):
        to_return = {
            "mes": self.mes,
            "colltmgkykvhxgij6": self.colltmgkykvhxgij6,
            "rmrp": self.rmrp,
            "poblacion_meta": self.poblacion_meta,
            "modalidad_impl": self.modalidad_impl,
            "IN2_01_RM_MM": self.IN2_01_RM_MM,
            "IN2_01_RM_HH": self.IN2_01_RM_HH,
            "IN2_01_RM_OTR": self.IN2_01_RM_OTR,
            "IN2_01_CA_MM": self.IN2_01_CA_MM,
            "IN2_01_CA_HH": self.IN2_01_CA_HH,
            "IN2_01_CA_OTR": self.IN2_01_CA_OTR,
            "IN2_01_CUAL": self.IN2_01_CUAL
        }
        return to_return

    def __repr__(self):
        return self.__str__()

    def to_json(self):
        return self.__str__()

class SubFormIN3_01:
    def __init__(self,
                 mes,
                 colltmgkykvhxgij6,
                 rmrp,
                 IN3_01,
                 IN3_01_CUAL
                 ):
        self.mes = mes
        self.colltmgkykvhxgij6 = colltmgkykvhxgij6
        self.rmrp = rmrp
        self.IN3_01 = IN3_01
        self.IN3_01_CUAL = IN3_01_CUAL

    def __str__(self):
        to_return = {
            "mes": self.mes,
            "colltmgkykvhxgij6": self.colltmgkykvhxgij6,
            "rmrp": self.rmrp,
            "IN3_01": self.IN3_01,
            "IN3_01_CUAL": self.IN3_01_CUAL
        }
        return to_return

    def __repr__(self):
        return self.__str__()

    def to_json(self):
        return self.__str__()
class SubFormIN3_02:
    def __init__(self,
                 mes,
                 colltmgkykvhxgij6,
                 rmrp,
                 IN3_02,
                 IN3_02_CUAL
                 ):
        self.mes = mes
        self.colltmgkykvhxgij6 = colltmgkykvhxgij6
        self.rmrp = rmrp
        self.IN3_02 = IN3_02
        self.IN3_02_CUAL = IN3_02_CUAL

    def __str__(self):
        to_return = {
            "mes": self.mes,
            "colltmgkykvhxgij6": self.colltmgkykvhxgij6,
            "rmrp": self.rmrp,
            "IN3_02": self.IN3_02,
            "IN3_02_CUAL": self.IN3_02_CUAL

        }
        return to_return

    def __repr__(self):
        return self.__str__()

    def to_json(self):
        return self.__str__()


class SubFormIN3_03:
    def __init__(self,
                 mes,
                 colltmgkykvhxgij6,
                 rmrp,
                 covid,
                 IN3_03,
                 IN3_03_CUAL
                 ):
        self.mes = mes
        self.colltmgkykvhxgij6 = colltmgkykvhxgij6
        self.rmrp = rmrp
        self.covid = covid
        self.IN3_03 = IN3_03
        self.IN3_03_CUAL = IN3_03_CUAL

    def __str__(self):
        to_return = {
            "mes": self.mes,
            "colltmgkykvhxgij6": self.colltmgkykvhxgij6,
            "rmrp": self.rmrp,
            "covid": self.covid,
            "IN3_03": self.IN3_03,
            "IN3_03_CUAL": self.IN3_03_CUAL

        }
        return to_return

    def __repr__(self):
        return self.__str__()

    def to_json(self):
        return self.__str__()


class SubFormS8_01:
    def __init__(self,
                 mes,
                 colltmgkykvhxgij6,
                 rmrp,
                 covid,
                 poblacion_meta,
                 S8_01_RM_NA,
                 S8_01_RM_NN,
                 S8_01_RM_MM,
                 S8_01_RM_HH,
                 S8_01_RM_OTR,
                 S8_01_RM_LGBT,
                 S8_01_CA_NA,
                 S8_01_CA_NN,
                 S8_01_CA_MM,
                 S8_01_CA_HH,
                 S8_01_CA_OTR,
                 S8_01_CA_LGBT,
                 S8_01_CUAL

                 ):
        self.mes = mes
        self.colltmgkykvhxgij6 = colltmgkykvhxgij6
        self.rmrp = rmrp
        self.covid = covid
        self.poblacion_meta = poblacion_meta
        self.S8_01_RM_NA = S8_01_RM_NA
        self.S8_01_RM_NN = S8_01_RM_NN
        self.S8_01_RM_MM = S8_01_RM_MM
        self.S8_01_RM_HH = S8_01_RM_HH
        self.S8_01_RM_OTR = S8_01_RM_OTR
        self.S8_01_RM_LGBT = S8_01_RM_LGBT
        self.S8_01_CA_NA = S8_01_CA_NA
        self.S8_01_CA_NN = S8_01_CA_NN
        self.S8_01_CA_MM = S8_01_CA_MM
        self.S8_01_CA_HH = S8_01_CA_HH
        self.S8_01_CA_OTR = S8_01_CA_OTR
        self.S8_01_CA_LGBT = S8_01_CA_LGBT
        self.S8_01_CUAL = S8_01_CUAL

    def __str__(self):
        to_return = {
            "mes": self.mes,
            "colltmgkykvhxgij6": self.colltmgkykvhxgij6,
            "rmrp": self.rmrp,
            "covid": self.covid,
            "poblacion_meta": self.poblacion_meta,
            "S8_01_RM_NA": self.S8_01_RM_NA,
            "S8_01_RM_NN": self.S8_01_RM_NN,
            "S8_01_RM_MM": self.S8_01_RM_MM,
            "S8_01_RM_HH": self.S8_01_RM_HH,
            "S8_01_RM_OTR": self.S8_01_RM_OTR,
            "S8_01_RM_LGBT": self.S8_01_RM_LGBT,
            "S8_01_CA_NA": self.S8_01_CA_NA,
            "S8_01_CA_NN": self.S8_01_CA_NN,
            "S8_01_CA_MM": self.S8_01_CA_MM,
            "S8_01_CA_HH": self.S8_01_CA_HH,
            "S8_01_CA_OTR": self.S8_01_CA_OTR,
            "S8_01_CA_LGBT": self.S8_01_CA_LGBT,
            "S8_01_CUAL": self.S8_01_CUAL
        }
        return to_return

    def __repr__(self):
        return self.__str__()

    def to_json(self):
        return self.__str__()


class SubFormWA1_04:
    def __init__(self,
                 mes,
                 colltmgkykvhxgij6,
                 rmrp,
                 covid,
                 wash_individual, ## No
                 poblacion_meta,
                 modalidad_impl, ## cb53mqwkykwwihkk8
                 WA1_04_RM_NA,
                 WA1_04_RM_NN,
                 WA1_04_RM_MM,
                 WA1_04_RM_HH,
                 WA1_04_CA_NA,
                 WA1_04_CA_NN,
                 WA1_04_CA_MM,
                 WA1_04_CA_HH,
                 WA1_04_CUAL

                 ):
        self.mes = mes
        self.colltmgkykvhxgij6 = colltmgkykvhxgij6
        self.rmrp = rmrp
        self.covid = covid
        self.wash_individual = wash_individual
        self.poblacion_meta = poblacion_meta
        self.modalidad_impl = modalidad_impl
        self.WA1_04_RM_NA = WA1_04_RM_NA
        self.WA1_04_RM_NN = WA1_04_RM_NN
        self.WA1_04_RM_MM = WA1_04_RM_MM
        self.WA1_04_RM_HH = WA1_04_RM_HH
        self.WA1_04_CA_NA = WA1_04_CA_NA
        self.WA1_04_CA_NN = WA1_04_CA_NN
        self.WA1_04_CA_MM = WA1_04_CA_MM
        self.WA1_04_CA_HH = WA1_04_CA_HH
        self.WA1_04_CUAL = WA1_04_CUAL

    def __str__(self):
        to_return = {
            "mes": self.mes,
            "colltmgkykvhxgij6": self.colltmgkykvhxgij6,
            "rmrp": self.rmrp,
            "covid": self.covid,
            "wash_individual": self.wash_individual,
            "poblacion_meta": self.poblacion_meta,
            "modalidad_impl": self.modalidad_impl,
            "WA1_04_RM_NA": self.WA1_04_RM_NA,
            "WA1_04_RM_NN": self.WA1_04_RM_NN,
            "WA1_04_RM_MM": self.WA1_04_RM_MM,
            "WA1_04_RM_HH": self.WA1_04_RM_HH,
            "WA1_04_CA_NA": self.WA1_04_CA_NA,
            "WA1_04_CA_NN": self.WA1_04_CA_NN,
            "WA1_04_CA_MM": self.WA1_04_CA_MM,
            "WA1_04_CA_HH": self.WA1_04_CA_HH,
            "WA1_04_CUAL": self.WA1_04_CUAL
        }
        return to_return

    def __repr__(self):
        return self.__str__()

    def to_json(self):
        return self.__str__()