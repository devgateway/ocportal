package org.devgateway.ocds.web.convert;

import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.types.ObjectId;
import org.devgateway.ocds.persistence.mongo.Document;
import org.devgateway.toolkit.persistence.dao.FileMetadata;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.net.URI;

public interface MongoFileStorageService {

    Document storeFileAndReferenceAsDocument(FileMetadata fm, Document.DocumentType documentType);

    Document storeFileAndReferenceAsDocument(FileMetadata fm, String documentType);

    GridFSFile retrieveFile(ObjectId id);

    URI createURL(String url);

    FileMetadata storeFile(FileMetadata fileMetadata);

    Query MAKUENI_FILES_QUERY = new Query(Criteria.where("metadata.source").is("makueniFiles"));


}
