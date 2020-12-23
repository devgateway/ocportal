import PropTypes from "prop-types";

export const commonTenderTabTypes = {
  styling: PropTypes.object.isRequired,
  fiscalYear: PropTypes.object.isRequired,
  department: PropTypes.object.isRequired,
  translations: PropTypes.object.isRequired,
  isFeatureVisible: PropTypes.func.isRequired,
};

const defaultSingleTenderTabTypes = {
  ...commonTenderTabTypes,
  data: PropTypes.array,
  tenderTitle: PropTypes.string.isRequired,
};

export default defaultSingleTenderTabTypes;
