import {OverlayTrigger, Tooltip} from "react-bootstrap";
import React from "react";

const FileDownloadLinks = ({files, useDash}) => {
  if (useDash && (files || []).length === 0) {
    return "-"
  }
  return (files || []).map(doc => <div key={doc._id || doc.id}>
      <OverlayTrigger
        placement="bottom"
        overlay={
          <Tooltip id="download-tooltip">
            Click to download the file
          </Tooltip>
        }>

        <a className="download-file" href={doc.url} target="_blank">
          <i className="glyphicon glyphicon-download"/>
          <span>{doc.name}</span>
        </a>
      </OverlayTrigger>
    </div>)
}

export default FileDownloadLinks