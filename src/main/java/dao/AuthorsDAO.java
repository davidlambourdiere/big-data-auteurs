package dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import model.Author;
import org.bson.Document;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Slf4j
public class AuthorsDAO{
    private final MongoCollection<Document> collection;

    public AuthorsDAO(){
        MongoDatabase db = DBHelper.getDatabase();
        collection = db.getCollection(DBHelper.COLLECTION_AUTHORS);
    }

    public Author insert(Author author) {
        Document authorAsDocument = author.asDocument();
        collection.insertOne(authorAsDocument);
        return documentToAuthor(authorAsDocument);
    }
    public void insert(ArrayList<Author> authors){
        collection.insertMany(authors.stream().map(x-> x.asDocument()).collect(Collectors.toList()));
    }

    public Author findByFirstnameAndLastname(String firstname, String lastname){
        Document document = collection.find(new Document().append("firstname", firstname).append("lastname", lastname)).first();
        if(document != null)
            return documentToAuthor(document);
        else
            return null;
    }
    public Author documentToAuthor(Document document){
        return Author.builder()
                .id(document.get("_id").toString())
                .firstname(document.get("firstname").toString())
                .lastname(document.get("lastname").toString())
                .build();
    }
}
