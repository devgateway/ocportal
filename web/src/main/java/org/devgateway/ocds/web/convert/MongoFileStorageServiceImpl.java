package org.devgateway.ocds.web.convert;

import com.google.common.io.ByteSource;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.apache.commons.codec.digest.DigestUtils;
import org.bson.types.ObjectId;
import org.devgateway.ocds.persistence.mongo.Document;
import org.devgateway.toolkit.persistence.dao.FileMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(MongoFileStorageServiceImpl.class);


    public static final String DOWNLOAD_PREFIX = "http://localhost:8090/api/file/";

    @Autowired
    private GridFsOperations gridFsOperations;

    public String computeFileMd5(FileMetadata fileMetadata) {
        return DigestUtils.md5Hex(fileMetadata.getContent().getBytes());
    }

    public FileMetadata storeFile(final FileMetadata fileMetadata) {
        try {
            if (ObjectUtils.isEmpty(fileMetadata)
                    || ObjectUtils.isEmpty(fileMetadata.getContent())
                    || ObjectUtils.isEmpty(fileMetadata.getContent().getBytes())) {
                return null;
            }

            final DBObject metaData = new BasicDBObject();
            String md5 = computeFileMd5(fileMetadata);
            GridFSFile existingFile = gridFsOperations.findOne(new Query(Criteria.where("md5").is(md5)));
            ObjectId objId;

            //will not store the same file twice
            if (existingFile != null && existingFile.getFilename().equals(fileMetadata.getName())) {
                objId = existingFile.getObjectId();
            } else {
                final InputStream is = ByteSource.wrap(fileMetadata.getContent().getBytes()).openStream();
                objId = gridFsOperations.store(
                        is, fileMetadata.getName(), fileMetadata.getContentType(), metaData);
                is.close();
            }

            fileMetadata.setUrl(DOWNLOAD_PREFIX + objId);

            return fileMetadata;
        } catch (IOException e) {
            logger.error("Error wile saving a file.", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Document storeFileAndReferenceAsDocument(FileMetadata fm, String documentType) {
        FileMetadata fileMetadata = storeFile(fm);

        Document doc = new Document();
        if (fileMetadata.getCreatedDate().isPresent()) {
            doc.setDatePublished(java.sql.Date.valueOf(fileMetadata.getCreatedDate().get().toLocalDate()));
        }
        if (fileMetadata.getLastModifiedDate().isPresent()) {
            doc.setDateModified(java.sql.Date.valueOf(fileMetadata.getLastModifiedDate().get().toLocalDate()));
        }
        doc.setTitle(fileMetadata.getName());
        doc.setFormat(fileMetadata.getContentType());
        doc.setUrl(createURL(fileMetadata.getUrl()));
        doc.setId(fileMetadata.getId().toString());
        doc.setDocumentType(documentType);

        return doc;
    }

    @Override
    public Document storeFileAndReferenceAsDocument(FileMetadata fm, Document.DocumentType documentType) {
        return storeFileAndReferenceAsDocument(fm, documentType.toString());
    }


    @Override
    public GridFSFile retrieveFile(ObjectId id) {
        return gridFsOperations.findOne(new Query(Criteria.where(Fields.UNDERSCORE_ID).is(id)));
    }

    @Override
    public URI createURL(String url) {
        try {
            return new URI(url);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
