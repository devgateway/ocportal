import { API_ROOT, OCE } from '../state/oce-state';
import {fromJS} from "immutable";

const fmState = OCE.substate({ name: 'fm' });

const fmEP = fmState.input({
  name: 'fmEP',
  initial: `${API_ROOT}/fm/featureProperties`
});

const fmFilter = fmState.input({
  name: 'fmFilter',
  initial: fromJS({
    "fmPrefixes": ["viz.", "tenderForm", "tenderProcessForm", "tenderQuotationEvaluationForm",
      "professionalOpinionForm", "awardNotificationForm", "awardAcceptanceForm", "contractForm",
      "administratorReportForm", "meReportForm", "pmcReportForm", "inspectionReportForm", "projectForm",
      "paymentVoucherForm"]
  })
});

const fmRemote = fmState.remote({
  name: 'fmRemote',
  url: fmEP,
  params: fmFilter
});

export const fmList = fmState.mapping({
  name: 'fmList',
  deps: [fmRemote],
  mapper: list =>
    list
      .filter(fm => fm.visible)
      .map(fm => fm.name)
});
