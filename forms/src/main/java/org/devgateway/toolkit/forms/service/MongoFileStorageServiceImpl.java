package org.devgateway.toolkit.forms.service;

import com.google.common.io.ByteSource;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.types.ObjectId;
import org.devgateway.ocds.persistence.mongo.Document;
import org.devgateway.toolkit.persistence.dao.FileMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

@Service
public class MongoFileStorageServiceImpl implements MongoFileStorageService {

    public static final String DOWNLOAD_PREFIX = "/ocdsFile/";

    @Autowired
    private GridFsOperations gridFsOperations;

    @Override
    public Document storeFileAndReferenceAsDocument(FileMetadata fm, Document.DocumentType documentType) {
        try {
            if (ObjectUtils.isEmpty(fm) || ObjectUtils.isEmpty(fm.getContent())) {
                return null;
            }
            InputStream is = ByteSource.wrap(fm.getContent().getBytes()).openStream();
            ObjectId objId = gridFsOperations.store(is, fm.getName(), fm.getContentType());
            is.close();

            Document doc = new Document();
            doc.setDateModified(java.sql.Date.valueOf(fm.getLastModifiedDate().get().toLocalDate()));
            doc.setTitle(fm.getName());
            doc.setFormat(fm.getContentType());
            doc.setUrl(createURL(objId));
            doc.setId(objId.toString());
            doc.setDocumentType(documentType.toString());

            return doc;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public GridFSFile retrieveFile(ObjectId id) {
        return gridFsOperations.findOne(new Query(Criteria.where(Fields.UNDERSCORE_ID).is(id)));
    }

    @Override
    public URI createURL(ObjectId id) {
        try {
            return new URI(DOWNLOAD_PREFIX + id);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
