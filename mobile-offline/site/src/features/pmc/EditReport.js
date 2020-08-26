import React, {useEffect, useState} from 'react';
import {useDispatch, useSelector} from 'react-redux';
import Select from 'react-select'
import DatePicker from 'react-datepicker'
import {cancelEditReport} from "./pmcReportsSlice";
import {selectMetadata} from "./metadataSlice";

import "react-datepicker/dist/react-datepicker.css";
import "./../../react-datepicker.css";

// TODO think of view
// TODO implement actual save
// TODO validation & error reporting
// TODO prevent data loss
export function EditReport() {
    const metadata = useSelector(selectMetadata);
    const subCounties = metadata.ref["Subcounty"];
    const fiscalYears = metadata.ref["FiscalYear"];
    const departments = metadata.ref["Department"];
    const tenders = metadata.ref["Tender"];
    const wards = metadata.ref["Ward"];
    const pmcStaff = metadata.ref["PMCStaff"];
    const designations = metadata.ref["Designation"];
    const projectClosureHandoverOptions = metadata.ref["ProjectClosureHandover"];

    const dispatch = useDispatch();

    function filterWards(subCounties, wards) {
        return (wards || []).filter(w => (subCounties || []).some(sc => w.parentId === sc.id));
    }

    function filterTenders(fiscalYear, department, tenders) {
        if (!department || !fiscalYear) {
            return [];
        }
        return (tenders || []).filter(t => t.fyId === fiscalYear.id && t.deptId === department.id);
    }

    const [report, setReport] = useState({authorizePayment: false});

    const availableWards = filterWards(report.subCounties, wards);
    const availableTenders = filterTenders(report.fiscalYear, report.department, tenders);

    const fieldChanged = e => {
        const value = e.target.type === 'checkbox' ? e.target.checked : e.target.value;
        setReport({
            ...report,
            [e.target.name]: value
        });
    };

    useEffect(() => {
        setReport(report => ({
            ...report,
            wards: filterWards(report.subCounties, report.wards)
        }));
    }, [report.subCounties]);

    useEffect(() => {
        setReport(report => {
            const tenders = filterTenders(report.fiscalYear, report.department, report.tender ? [ report.tender ] : []);
            return {
                ...report,
                tender: tenders.length === 0 ? null : tenders[0]
            }
        });
    }, [report.fiscalYear, report.department]);

    const handleCancel = (e) => {
        e.preventDefault();
        dispatch(cancelEditReport());
    };

    const handleSave = (e) => {
        e.preventDefault();
        // TODO set status
        console.log(report);
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        // TODO set status
        console.log(report);
    };

    return (
        <div className="container-fluid pt-3 pb-3">
            <h1>New report</h1>

            <SelectCategoryField label="Fiscal Year" name="fiscalYear" value={report.fiscalYear} options={fiscalYears}
                                 onChange={fieldChanged} />

            <SelectCategoryField label="Department" name="department" value={report.department} options={departments}
                                 onChange={fieldChanged} />

            <SelectCategoryField label="Tender" name="tender" value={report.tender} options={availableTenders}
                                 onChange={fieldChanged} />

            <DateField label="Date" name="date" value={report.date} onChange={fieldChanged} />

            <SelectCategoryField label="Subcounties" name="subCounties" value={report.subCounties} options={subCounties}
                                 isMulti onChange={fieldChanged} />

            <SelectCategoryField label="Wards" name="wards" value={report.wards} options={availableWards}
                                 isMulti onChange={fieldChanged} />

            <PMCMembers name="pmcMembers" value={report.pmcMembers} pmcStaff={pmcStaff} designations={designations}
                        onChange={fieldChanged} />

            <CheckboxField label="Authorize payment" name="authorizePayment" value={report.authorizePayment}
                           onChange={fieldChanged} />

            <Notes name="pmcNotes" value={report.pmcNotes} onChange={fieldChanged} />

            <SelectCategoryField label="Project Closure and Handover" name="projectClosureAndHandover"
                                 value={report.projectClosureAndHandover} options={projectClosureHandoverOptions}
                                 onChange={fieldChanged} />

            <CheckboxField label="eSignature" name="signature" value={report.signature}
                           onChange={fieldChanged} />

            <TextField label="eSignature First Name & Last Name" name="signatureNames" value={report.signatureNames}
                       onChange={fieldChanged} />

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

    const pmcMemberRows = pmcMembers.map(
        (m, idx) => (
            <div key={idx} className="form-group">
                <p>
                    Member {idx+1}

                    <button type="button" className="btn btn-sm btn-danger float-right"
                            onClick={removePMCMember(idx)}>
                        Remove PMC Member
                    </button>
                </p>

                <SelectCategoryField label="PMC Staff" name="pmcStaff"
                                     value={m.pmcStaff} options={props.pmcStaff}
                                     onChange={changeField(idx)} />

                <SelectCategoryField label="Designation" name="designation"
                                     value={m.designation} options={props.designations}
                                     onChange={changeField(idx)} />
            </div>
        )
    );

    return (
        <div className="form-group">
            <label>PMC Members</label>

            {pmcMemberRows}

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

    const rows = notes.map(
        (note, idx) => (
            <div key={idx} className="form-group">
                <p>
                    Note {idx+1}

                    <button type="button" className="btn btn-sm btn-danger float-right"
                            onClick={() => onChange(notes.filter((m, sidx) => sidx !== idx))}>
                        Remove Note
                    </button>
                </p>

                <textarea name="notes" value={note.notes} onChange={changeField(idx)} rows="2"
                          className="form-control" />
            </div>
        )
    );

    return (
        <div className="form-group">
            <label>Notes</label>

            {rows}

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
    const onChange = value => {
        const event = {
            target: {
                name: props.name,
                value: value
            }
        };
        props.onChange(event);
    }

    return (
        <div className="form-group">
            <label>{props.label}</label>
            <SelectCategory value={props.value} options={props.options} isMulti={props.isMulti}
                            onChange={onChange} />
        </div>
    );
}

function SelectCategory(props) {
    return <Select getOptionValue={option => option['id']} {...props} />
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

    return (
        <div className="form-group">
            <label>{props.label}</label>
            <div>
                <DatePicker selected={Date.parse(props.value)} onChange={dateChanged}
                            customInput={<input type="text" className="form-control" />}
                            dateFormat="dd/MM/yyyy"/>
            </div>
        </div>
    );
}

function CheckboxField(props) {
    const id = props.name;
    return (
        <div className="form-group form-check">
            <input name={props.name} id={id} type="checkbox" className="form-check-input"
                   checked={props.value} onChange={props.onChange} />
            <label className="form-check-label" htmlFor={id}>{props.label}</label>
        </div>
    );
}

function TextField(props) {
    return (
        <div className="form-group">
            <label>{props.label}</label>
            <input name={props.name} type="text" className="form-control"
                   checked={props.value} onChange={props.onChange} />
        </div>
    );
}
