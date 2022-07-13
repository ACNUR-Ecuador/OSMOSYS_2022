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
                 covid,
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
        self.covid = covid
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
            "covid": self.covid,
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
                 covid,
                 espacion_apoyo,
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


class SubFormPR6_01:
    def __init__(self,
                 mes,
                 colltmgkykvhxgij6,
                 rmrp,
                 covid,
                 modalidad_impl,
                 PR6_01,
                 PR6_01_CUAL
                 ):
        self.mes = mes,
        self.colltmgkykvhxgij6 = colltmgkykvhxgij6,
        self.rmrp = rmrp,
        self.covid = covid,
        self.modalidad_impl = modalidad_impl,
        self.PR6_01 = PR6_01,
        self.PR6_01_CUAL = PR6_01_CUAL

    def __str__(self):
        to_return = {
            "mes": self.mes,
            "colltmgkykvhxgij6": self.colltmgkykvhxgij6,
            "rmrp": self.rmrp,
            "covid": self.covid,
            "modalidad_impl": self.modalidad_impl,
            "PR6_01": self.PR6_01,
            "PR6_01_CUAL": self.PR6_01_CUAL
        }
        return to_return

    def __repr__(self):
        return self.__str__()

    def to_json(self):
        return self.__str__()


class SubFormPRPI1_01_35:
    def __init__(self,
                 mes,
                 colltmgkykvhxgij6,
                 rmrp,
                 covid,
                 espacion_apoyo,
                 poblacion_meta,
                 modalidad_impl,
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
        self.covid = covid
        self.espacion_apoyo = espacion_apoyo
        self.poblacion_meta = poblacion_meta
        self.modalidad_impl = modalidad_impl
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
            "covid": self.covid,
            "espacion_apoyo": self.espacion_apoyo,
            "poblacion_meta": self.poblacion_meta,
            "modalidad_impl": self.modalidad_impl,
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


class SubFormPRPI1_01_85:
    def __init__(self,
                 mes,
                 colltmgkykvhxgij6,
                 rmrp,
                 covid,
                 espacion_apoyo,
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
        self.covid = covid
        self.espacion_apoyo = espacion_apoyo
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
            "covid": self.covid,
            "espacion_apoyo": self.espacion_apoyo,
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
                 covid,
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
        self.covid = covid
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
            "covid": self.covid,
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
