package dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import model.Link;
import org.bson.Document;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class LinksDAO {
    private final MongoCollection<Document> collection;

    public LinksDAO(){
        MongoDatabase db = DBHelper.getDatabase();
        collection = db.getCollection(DBHelper.COLLECTION_LINKS);
    }

    public Link insert(Link Link) {
        Document linkAsDocument = Link.asDocument();
        collection.insertOne(linkAsDocument);
        return documentToLink(linkAsDocument);
    }
    public void insert(ArrayList<Link> Links){
        collection.insertMany(Links.stream().map(x-> x.asDocument()).collect(Collectors.toList()));
    }

    public Link find(Link link){
        Document document = collection.find(link.asDocument()).first();
        Document reverseDocument = collection.find(link.reverse().asDocument()).first();
        if(document != null)
            return link;
        else if (reverseDocument != null)
            return link.reverse();
        else
            return null;
    }
    public Link documentToLink(Document document){
        return Link.builder()
                .node1(document.get("node1").toString())
                .node2(document.get("node2").toString())
                .publicationYear(document.get("publicationYear").toString())
                .build();
    }
}
