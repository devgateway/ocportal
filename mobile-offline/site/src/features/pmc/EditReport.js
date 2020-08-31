import React, {useEffect, useState} from 'react';
import {useSelector} from 'react-redux';
import Select from 'react-select'
import DatePicker from 'react-datepicker'
import {Prompt, useHistory, useParams} from "react-router-dom";
import {selectPMCReports} from "./pmcReportsSlice";
import {selectMetadata} from "./metadataSlice";
import {DATE_FORMAT} from "../../app/constants";
import _ from "lodash";

import "react-datepicker/dist/react-datepicker.css";
import "./../../react-datepicker.css";

const scrollToFirstError = () => {
    let elems = document.getElementsByClassName("is-invalid");
    if (elems && elems.length) {
        window.scrollTo(0, elems[0].parentElement.offsetTop);
    }
}

// TODO think of view
// TODO implement actual save
export function EditReport(props) {
    const pmcReports = useSelector(selectPMCReports);
    const metadata = useSelector(selectMetadata);
    const subCounties = metadata.ref["Subcounty"];
    const fiscalYears = metadata.ref["FiscalYear"];
    const departments = metadata.ref["Department"];
    const tenders = metadata.ref["Tender"];
    const tendersById = metadata.ref["TenderById"];
    const wards = metadata.ref["Ward"];
    const pmcStaff = metadata.ref["PMCStaff"];
    const designations = metadata.ref["Designation"];
    const projectClosureHandoverOptions = metadata.ref["ProjectClosureHandover"];

    const [isBlocking, setIsBlocking] = useState(false);

    const filterWards = (subCountyIds, wards) => {
        return (wards || []).filter(w => (subCountyIds || []).some(scId => w.parentId === scId));
    };

    function filterTenders(fyId, deptId, tenders) {
        if (!deptId || !fyId) {
            return [];
        }
        return (tenders || []).filter(t => t.fyId === fyId && t.deptId === deptId);
    }

    const { reportId } = useParams();
    const originalReport = pmcReports.reports[reportId];

    const tender = tendersById[(originalReport || {}).tenderId] || {};

    const [report, setReport] = useState({
        authorizePayment: false,
        signature: false,
        fyId: tender.fyId,
        deptId: tender.deptId,
        ...originalReport
    });

    const [errors, setErrors] = useState({});
    const [submit, setSubmit] = useState(false);

    const availableWards = filterWards(report.subcountyIds, wards);
    const availableTenders = filterTenders(report.fyId, report.deptId, tenders);

    const validate = (report) => {
        let errors = {};

        const notNull = (errors, obj, field, message) => errors[field] = obj[field] ? undefined : message;
        const nonEmptyArray = (errors, obj, field, message) => {
            const array = obj[field];
            errors[field] = array && array.length ? undefined : message;
        }

        notNull(errors, report, 'fyId', 'Required');
        notNull(errors, report, 'deptId', 'Required');
        notNull(errors, report, 'tenderId', 'Required');
        notNull(errors, report, 'reportDate', 'Required');
        nonEmptyArray(errors, report, 'subcountyIds', 'Please select at least one subcounty');
        nonEmptyArray(errors, report, 'wardIds', 'Please select at least one ward');

        nonEmptyArray(errors, report, 'pmcMembers', 'Please add at least one PMC member');
        if (report.pmcMembers && report.pmcMembers.length) {
            errors.pmcMembersArray = report.pmcMembers.map(member => {
                let memberErrors = {};
                notNull(memberErrors, member, 'staffId', 'Required');
                notNull(memberErrors, member, 'designationId', 'Required');
                return memberErrors;
            });
        }

        nonEmptyArray(errors, report, 'pmcNotes', 'Please add at least one note');
        if (report.pmcNotes && report.pmcNotes.length) {
            errors.pmcNotesArray = report.pmcNotes.map(note => {
                let noteErrors = {};
                notNull(noteErrors, note, 'text', 'Required');
                return noteErrors;
            });
        }

        if (report.signature !== true) {
            errors.signature = 'Report must be signed';
        }
        notNull(errors, report, 'signatureNames', 'Required');

        return errors;
    };

    const fieldChanged = e => {
        const value = e.target.type === 'checkbox' ? e.target.checked : e.target.value;
        const newReport = {
            ...report,
            [e.target.name]: value
        };
        setReport(newReport);
        setIsBlocking(true);

        if (!_.isEmpty(errors)) {
            setErrors(validate(newReport));
        }
    };

    useEffect(() => {
        setReport(report => {
            let ids = {};
            filterWards(report.subcountyIds, wards).forEach(w => ids[w.id] = true);
            return {
                ...report,
                wardIds: (report.wardIds || []).filter(wardId => ids[wardId])
            }
        });
    }, [report.subcountyIds, wards]);

    useEffect(() => {
        setReport(report => {
            const tenders = filterTenders(report.fyId, report.deptId, report.tender ? [ report.tender ] : []);
            return {
                ...report,
                tender: tenders.length === 0 ? null : tenders[0]
            }
        });
    }, [report.fyId, report.deptId]);

    const history = useHistory();

    const handleCancel = (e) => {
        e.preventDefault();
        history.goBack();
    };

    const handleSave = (e) => {
        e.preventDefault();
        // TODO set status
        console.log(report);
        setIsBlocking(false);

        history.goBack();
    };

    useEffect(() => {
        if (submit) {
            scrollToFirstError();
        }
        setSubmit(false);
    }, [submit]);

    const handleSubmit = (e) => {
        e.preventDefault();

        const errors = validate(report);
        setErrors(errors);
        setSubmit(true);

        // TODO set status
        console.log(report);

        if (_.isEmpty(errors)) {
            setIsBlocking(false);
            history.goBack();
        }
    };

    return (
        <div className="container-fluid pt-3 pb-3">
            <Prompt
                when={isBlocking}
                message='Your changes will be lost. Are you sure?' />

            <h1>{originalReport ? "Edit report" : "New report"} </h1>

            <SelectCategoryField label="Fiscal Year" name="fyId" value={report.fyId} options={fiscalYears}
                                 onChange={fieldChanged} errors={errors} />

            <SelectCategoryField label="Department" name="deptId" value={report.deptId} options={departments}
                                 onChange={fieldChanged} errors={errors} />

            <SelectCategoryField label="Tender" name="tenderId" value={report.tenderId} options={availableTenders}
                                 onChange={fieldChanged} errors={errors} />

            <DateField label="Date" name="reportDate" value={report.reportDate} onChange={fieldChanged}
                       errors={errors} />

            <SelectCategoryField label="Subcounties" name="subcountyIds" value={report.subcountyIds} options={subCounties}
                                 isMulti onChange={fieldChanged} errors={errors} />

            <SelectCategoryField label="Wards" name="wardIds" value={report.wardIds} options={availableWards}
                                 isMulti onChange={fieldChanged} errors={errors} />

            <PMCMembers name="pmcMembers" value={report.pmcMembers} pmcStaff={pmcStaff} designations={designations}
                        onChange={fieldChanged} errors={errors} />

            <CheckboxField label="Authorize payment" name="authorizePayment" value={report.authorizePayment}
                           onChange={fieldChanged} errors={errors} />

            <Notes name="pmcNotes" value={report.pmcNotes} onChange={fieldChanged} errors={errors} />

            <SelectCategoryField label="Project Closure and Handover" name="projectClosureHandoverIds" isMulti
                                 value={report.projectClosureHandoverIds} options={projectClosureHandoverOptions}
                                 onChange={fieldChanged} errors={errors} />

            <CheckboxField label="eSignature" name="signature" value={report.signature}
                           onChange={fieldChanged} errors={errors} />

            <TextField label="eSignature First Name & Last Name" name="signatureNames" value={report.signatureNames}
                       onChange={fieldChanged} errors={errors} />

            <div>
                <button type="button" className="btn btn-primary" onClick={handleSave}>Save as Draft</button>
                &nbsp;
                <button type="button" className="btn btn-success" onClick={handleSubmit}>Submit</button>
                &nbsp;
                <button type="button" className="btn btn-secondary" onClick={handleCancel}>Cancel</button>
            </div>
        </div>
    );
}

function PMCMembers(props) {
    const pmcMembers = props.value || [];

    const onChange = value => props.onChange({
        target: {
            name: props.name,
            value: value
        }
    });

    const changeField = (idx) => e => {
        const newValue = pmcMembers.map((m, sidx) => {
            return (sidx !== idx) ? m : {...m, [ e.target.name ]: e.target.value};
        })
        onChange(newValue);
    };

    const removePMCMember = (idx) => () => {
        const newValue = pmcMembers.filter((m, sidx) => sidx !== idx);
        onChange(newValue);
    };

    const addPMCMember = () => {
        const newValue = pmcMembers.concat({});
        onChange(newValue);
    };

    const error = (props.errors || {})[props.name];
    const pmcMemberErrors = (props.errors.pmcMembersArray || []);

    const pmcMemberRows = pmcMembers.map(
        (m, idx) => {
            const pmcMemberError = pmcMemberErrors[idx];
            return (
                <div key={idx} className="form-group">
                    <p>
                        Member #{idx + 1}

                        <button type="button" className="btn btn-sm btn-danger float-right"
                                onClick={removePMCMember(idx)}>
                            Remove PMC Member
                        </button>
                    </p>

                    <SelectCategoryField label="PMC Staff" name="staffId"
                                         value={m.staffId} options={props.pmcStaff} errors={pmcMemberError}
                                         onChange={changeField(idx)}/>

                    <SelectCategoryField label="Designation" name="designationId"
                                         value={m.designationId} options={props.designations} errors={pmcMemberError}
                                         onChange={changeField(idx)}/>
                </div>
            );
        }
    );

    return (
        <div className="form-group">
            <label>PMC Members</label>

            <div className={(error ? "is-invalid" : "")}>
                {pmcMemberRows}
            </div>

            {error && <div className="list-input invalid-feedback">{error}</div>}

            <div>
                <button type="button" className="btn btn-success" onClick={addPMCMember}>Add PMC Member</button>
            </div>
        </div>
    );
}

function Notes(props) {
    const notes = props.value || [];

    const onChange = value => props.onChange({
        target: {
            name: props.name,
            value: value
        }
    });

    const changeField = (idx) => e => {
        const newValue = notes.map((note, sidx) => {
            return (sidx !== idx) ? note : {...note, [ e.target.name ]: e.target.value};
        })
        onChange(newValue);
    };

    const error = (props.errors || {})[props.name];
    const noteErrors = (props.errors.pmcNotesArray || []);

    const rows = notes.map(
        (note, idx) => {
            const textError = (noteErrors[idx] || {})["text"];

            return (
            <div key={idx} className="form-group">
                <p>
                    Note #{idx+1}

                    <button type="button" className="btn btn-sm btn-danger float-right"
                            onClick={() => onChange(notes.filter((m, sidx) => sidx !== idx))}>
                        Remove Note
                    </button>
                </p>

                <textarea name="text" value={note.text} onChange={changeField(idx)} rows="2"
                          className={"form-control" + (textError ? " is-invalid" : "")} />

                {textError && <div className="invalid-feedback">{textError}</div>}
            </div>
        )}
    );

    return (
        <div className="form-group">
            <label>Notes</label>

            <div className={(error ? "is-invalid" : "")}>
                {rows}
            </div>

            {error && <div className="list-input invalid-feedback">{error}</div>}

            <div>
                <button type="button" className="btn btn-success"
                        onClick={() => onChange(notes.concat({}))}>
                    Add Note
                </button>
            </div>
        </div>
    );

}

function SelectCategoryField(props) {
    const onChange = objVal => {
        let idVal;
        if (props.isMulti) {
            idVal = (objVal || []).map(el => el.id);
        } else {
            idVal = (objVal || {}).id;
        }
        const event = {
            target: {
                name: props.name,
                value: idVal
            }
        };
        props.onChange(event);
    }

    let objValue;
    if (props.isMulti) {
        objValue = (props.value || []).map(valueId => ({
            id: valueId,
            label: (props.options.find(option => valueId === option.id) || {}).label
        }));
    } else {
        objValue = {
            id: props.value,
            label: (props.options.find(option => props.value === option.id) || {}).label
        };
    }

    const error = (props.errors || {})[props.name];

    const customStyles = {
        control: (provided, state) => ({
            ...provided,
            borderColor: error ? 'red' : provided.borderColor
        })
    };

    return (
        <div className="form-group">
            <label>{props.label}</label>
            <Select value={objValue} styles={customStyles} options={props.options} isMulti={props.isMulti}
                    onChange={onChange} getOptionValue={option => option['id']}
                    className={(error ? "is-invalid" : "")}/>
            {error && <div className="invalid-feedback">{error}</div>}
        </div>
    );
}

function DateField(props) {
    const dateChanged = value => {
        props.onChange({
            target: {
                name: props.name,
                value: value.toISOString().substring(0, 10)
            }
        });
    };

    const error = (props.errors || {})[props.name];

    return (
        <div className="form-group">
            <label>{props.label}</label>
            <div className={(error ? " is-invalid" : "")}>
                <DatePicker selected={Date.parse(props.value)} onChange={dateChanged}
                            customInput={
                                <input type="text" className={"form-control" + (error ? " is-invalid" : "")} />
                            }
                            dateFormat={DATE_FORMAT} />
            </div>
            {error && <div className="invalid-feedback">{error}</div>}
        </div>
    );
}

function CheckboxField(props) {
    const id = props.name;
    const error = (props.errors || {})[props.name];
    return (
        <div className="form-group form-check">
            <input name={props.name} id={id} type="checkbox"
                   className={"form-check-input" + (error ? " is-invalid" : "")}
                   checked={props.value} onChange={props.onChange} />
            <label className="form-check-label" htmlFor={id}>{props.label}</label>
            {error && <div className="invalid-feedback">{error}</div>}
        </div>
    );
}

function TextField(props) {
    const error = (props.errors || {})[props.name];
    return (
        <div className="form-group">
            <label>{props.label}</label>
            <input name={props.name} type="text" className={"form-control" + (error ? " is-invalid" : "")}
                   value={props.value || ''} onChange={props.onChange} />
            {error && <div className="invalid-feedback">{error}</div>}
        </div>
    );
}
