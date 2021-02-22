package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.bson.Document;

@Data
@Builder
@AllArgsConstructor
public class Link {
    String node1;
    String node2;
    String publicationYear;

    public Link reverse(){
        return Link.builder().node1(node2).node2(node1).publicationYear(publicationYear).build();
    }
    public Document asDocument() {
        return new Document()
                .append("node1", node1)
                .append("node2", node2)
                .append("publicationYear",publicationYear);
    }
    public String join(){
        return String.join(";",node1,node2,publicationYear);
    }
}
