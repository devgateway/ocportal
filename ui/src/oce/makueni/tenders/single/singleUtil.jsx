import PropTypes from "prop-types";

const defaultSingleTenderTabTypes = {
    styling: PropTypes.object.isRequired,
    fiscalYear: PropTypes.object.isRequired,
    department: PropTypes.object.isRequired,
    translations: PropTypes.object.isRequired,
    data: PropTypes.array,
    isFeatureVisible: PropTypes.func.isRequired,
    tenderTitle: PropTypes.string.isRequired,
};

export default defaultSingleTenderTabTypes;
