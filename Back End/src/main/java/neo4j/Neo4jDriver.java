package neo4j;

import org.neo4j.driver.internal.value.PathValue;
import org.neo4j.driver.v1.*;
import org.neo4j.driver.v1.types.Node;
import org.neo4j.driver.v1.types.Path;
import org.neo4j.driver.v1.types.Relationship;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.TransactionWork;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import util.CommonUtil;

import java.awt.*;
import java.util.*;
import java.util.List;

import static org.neo4j.driver.v1.Values.parameters;
@Component
public class Neo4jDriver {
    @Cacheable(cacheNames = "query",cacheManager = "cacheManager")
    public HashMap<String, List<HashMap<String,Object>>> getresult(String name1, String name2, int step1, int step2) {
        Driver driver = GraphDatabase.driver("bolt://45.77.214.60:7687",
                AuthTokens.basic("neo4j","pzkpfw38t"));
        Map<Long,String> map = new HashMap<>();
        HashMap<String, List<HashMap<String,Object>>> resultgraph = new HashMap<>();
        try(Session session = driver.session()){
            try (Transaction tx = session.beginTransaction()){
                StatementResult result = tx.run("Match p=(n:" + CommonUtil.getlabel(name1) + "{Name:$name1 })-[r*" + step1 + ".." + step2 + "]-(m:" + CommonUtil.getlabel(name2) + "{Name:$name2}) return p as nodesrelation",
                        parameters("name1",name1,"name2",name2));
                List<HashMap<String,Object>> allnodes = new ArrayList<>();
                List<HashMap<String,Object>> allrelations = new ArrayList<>();
                while(result.hasNext()){
                    Record record = result.next();
                    Path path = record.get("nodesrelation").asPath();
                    Iterable<Node> nodes = path.nodes();
                    for(Node node:nodes) {
                        HashMap<String,Object> nod = new HashMap();
                        nod.put("id",node.id());
                        nod.put("name",node.get("Name").toString());
                        if(!allnodes.contains(nod))
                            allnodes.add(nod);
                    }
                    Iterable<Relationship> relations = path.relationships();
                    for(Relationship relationship:relations) {
                        HashMap<String,Object> rela = new HashMap();
                        rela.put("source",relationship.startNodeId());
                        rela.put("target",relationship.endNodeId());
                        rela.put("type",relationship.type());
                        allrelations.add(rela);
                    }
                }
                resultgraph.put("nodes",allnodes);
                resultgraph.put("links",allrelations);

            }
        }
        driver.close();
        System.out.println(resultgraph);
        return resultgraph;

    }
    public HashMap<String, List<HashMap<String,Object>>> getOneNoderesult(String name1, int step1, int step2) {
        Driver driver = GraphDatabase.driver("bolt://45.77.214.60:7687",
                AuthTokens.basic("neo4j","pzkpfw38t"));
        Map<Long,String> map = new HashMap<>();
        HashMap<String, List<HashMap<String,Object>>> resultgraph = new HashMap<>();
        try(Session session = driver.session()){
            try (Transaction tx = session.beginTransaction()){
                StatementResult result = tx.run("Match p=(n:" + CommonUtil.getlabel(name1) + "{Name:$name1 })-[r*" + step1 + ".." + step2 + "]-(m) return p as nodesrelation",
                        parameters("name1",name1));
                List<HashMap<String,Object>> allnodes = new ArrayList<>();
                List<HashMap<String,Object>> allrelations = new ArrayList<>();
                while(result.hasNext()){
                    System.out.println(1);
                    Record record = result.next();
                    Path path = record.get("nodesrelation").asPath();
                    Iterable<Node> nodes = path.nodes();
                    for(Node node:nodes) {
                        HashMap<String,Object> nod = new HashMap();
                        nod.put("id",node.id());
                        nod.put("name",node.get("Name").toString());
                        if(!allnodes.contains(nod))
                            allnodes.add(nod);
                    }
                    Iterable<Relationship> relations = path.relationships();
                    for(Relationship relationship:relations) {
                        HashMap<String,Object> rela = new HashMap();
                        rela.put("source",relationship.startNodeId());
                        rela.put("target",relationship.endNodeId());
                        rela.put("type",relationship.type());
                        allrelations.add(rela);
                    }

                }
                resultgraph.put("nodes",allnodes);
                resultgraph.put("links",allrelations);

            }
        }
        driver.close();
        return resultgraph;
    }
    public HashMap<String, List<HashMap<String,Object>>> getAllNodes() {
        Driver driver = GraphDatabase.driver("bolt://45.77.214.60:7687",
                AuthTokens.basic("neo4j","pzkpfw38t"));
        Map<Long,String> map = new HashMap<>();
        HashMap<String, List<HashMap<String,Object>>> resultgraph = new HashMap<>();
        try(Session session = driver.session()) {
            try (Transaction tx = session.beginTransaction()) {
                StatementResult result = tx.run("Match p=(n)-[r]-(m) return p as nodesrelation");
                List<HashMap<String,Object>> allnodes = new ArrayList<>();
                List<HashMap<String,Object>> allrelations = new ArrayList<>();
                while(result.hasNext()){
                    Record record = result.next();
                    Path path = record.get("nodesrelation").asPath();
                    Iterable<Node> nodes = path.nodes();
                    for(Node node:nodes) {
                        HashMap<String,Object> nod = new HashMap();
                        nod.put("id",node.id());
                        nod.put("name",node.get("name").toString());
                        nod.put("type",node.get("type").toString());
                        nod.put("layer",node.get("layer").toString());
                        nod.put("performance",node.get("performance").toString());
                        if(!allnodes.contains(nod))
                            allnodes.add(nod);
                    }
                    Iterable<Relationship> relations = path.relationships();
                    for(Relationship relationship:relations) {
                        HashMap<String,Object> rela = new HashMap();
                        rela.put("source",relationship.startNodeId());
                        rela.put("target",relationship.endNodeId());
                        rela.put("type",relationship.type());
                        allrelations.add(rela);
                    }
                }
                resultgraph.put("nodes",allnodes);
                resultgraph.put("links",allrelations);
            }
        }
        driver.close();
        return resultgraph;
    }

    public HashMap<String, Object> addOneNode(String label, int id, String name, String type, String layer, String performance, ArrayList<Integer> relations) {
        Driver driver = GraphDatabase.driver("bolt://45.77.214.60:7687",
                AuthTokens.basic("neo4j","pzkpfw38t"));
        HashMap<String, Object> result = new HashMap<>();
        //System.out.println();
        result.put("state", "Operation failed");
        try(Session session = driver.session()) {
            StatementResult newID = session.run("CREATE (n: "+label+") "+
                            "SET n.id = $id "+"SET n.name = $name "+"SET n.type = $type "+"SET n.layer = $layer "+
                            "SET n.performance = $performance "+"return id(n)",
                    parameters("id",id,"name",name,"type",type,"layer",layer,"performance",performance));
            int trueId = newID.single().get(0).asInt();
            System.out.println(relations);
            for(int i=0; i<relations.size(); i++) {
                session.run("Start a=node("+trueId+"),b=node("+relations.get(i)+") Merge (a)-[r:rel{type:'Have'}]->(b)");
            }
            result.remove("state");
            result.put("state", "Operation succeeded");
        }
        driver.close();
        return result;
    }

    public HashMap<String, List<HashMap<String,Object>>> getNeighbors(String name) {
        Driver driver = GraphDatabase.driver("bolt://45.77.214.60:7687",
                AuthTokens.basic("neo4j","pzkpfw38t"));
        Map<Long,String> map = new HashMap<>();
        HashMap<String, List<HashMap<String,Object>>> resultgraph = new HashMap<>();
        try(Session session = driver.session()) {
            try (Transaction tx = session.beginTransaction()) {
                StatementResult result = tx.run("Match p=(n{name:$name})-[r]-(m) return p as nodesrelation", parameters("name",name));
                List<HashMap<String,Object>> allnodes = new ArrayList<>();
                List<HashMap<String,Object>> allrelations = new ArrayList<>();
                while(result.hasNext()){
                    Record record = result.next();
                    Path path = record.get("nodesrelation").asPath();
                    Iterable<Node> nodes = path.nodes();
                    for(Node node:nodes) {
                        HashMap<String,Object> nod = new HashMap();
                        nod.put("id",node.id());
                        nod.put("name",node.get("name").toString());
                        nod.put("type",node.get("type").toString());
                        nod.put("layer",node.get("layer").toString());
                        nod.put("performance",node.get("performance").toString());
                        if(!allnodes.contains(nod))
                            allnodes.add(nod);
                    }
                    Iterable<Relationship> relations = path.relationships();
                    for(Relationship relationship:relations) {
                        HashMap<String,Object> rela = new HashMap();
                        rela.put("source",relationship.startNodeId());
                        rela.put("target",relationship.endNodeId());
                        rela.put("type",relationship.type());
                        allrelations.add(rela);
                    }
                }
                resultgraph.put("nodes",allnodes);
                resultgraph.put("links",allrelations);
            }
        }
        driver.close();
        return resultgraph;
    }

    public HashMap<String, List<HashMap<String,Object>>> getLabel(String label) {
        Driver driver = GraphDatabase.driver("bolt://45.77.214.60:7687",
                AuthTokens.basic("neo4j","pzkpfw38t"));
        Map<Long,String> map = new HashMap<>();
        HashMap<String, List<HashMap<String,Object>>> resultgraph = new HashMap<>();
        try(Session session = driver.session()) {
            try (Transaction tx = session.beginTransaction()) {
                StatementResult result = tx.run("Match p=(n: "+label+")-[r]-(m) return p as nodesrelation");
                List<HashMap<String,Object>> allnodes = new ArrayList<>();
                List<HashMap<String,Object>> allrelations = new ArrayList<>();
                while(result.hasNext()){
                    Record record = result.next();
                    Path path = record.get("nodesrelation").asPath();
                    Iterable<Node> nodes = path.nodes();
                    for(Node node:nodes) {
                        HashMap<String,Object> nod = new HashMap();
                        nod.put("id",node.id());
                        nod.put("name",node.get("name").toString());
                        nod.put("type",node.get("type").toString());
                        nod.put("layer",node.get("layer").toString());
                        nod.put("performance",node.get("performance").toString());
                        if(!allnodes.contains(nod))
                            allnodes.add(nod);
                    }
                    Iterable<Relationship> relations = path.relationships();
                    for(Relationship relationship:relations) {
                        HashMap<String,Object> rela = new HashMap();
                        rela.put("source",relationship.startNodeId());
                        rela.put("target",relationship.endNodeId());
                        rela.put("type",relationship.type());
                        allrelations.add(rela);
                    }
                }
                resultgraph.put("nodes",allnodes);
                resultgraph.put("links",allrelations);
            }
        }
        driver.close();
        return resultgraph;
    }

    public static Boolean Deal(String keyName, Map<String, Object> map, HashMap hashMap) {
        //System.out.println(map);
        Iterator iter = map.entrySet().iterator();
        String curKeyName = keyName;
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            keyName = curKeyName;
            if (!keyName.equals(""))
                keyName = keyName + "-" + (String) entry.getKey();
            else keyName = keyName + (String) entry.getKey();
            boolean flag = true;
            Map<String, Object> map1;
            try {
                map1 = (Map<String, Object>) entry.getValue();
                Deal(keyName, map1, hashMap);
            } catch (ClassCastException e) {
                flag = false;
            }
            if (!flag) {
                boolean flag2 = true;
                String val = "";
                int value = -999;
                try {
                    try {
                        val = (String) entry.getValue();
                    } catch (ClassCastException e) {
                        value = (int) entry.getValue();
                    }
                } catch (ClassCastException e) {
                    flag2 = false;
                }
                if (flag2 && value == -999) {
                    //System.out.println(keyName + "  " + val);
                    hashMap.put(keyName, val);
                } else if (flag2 && value != -999) {
                    //System.out.println(keyName + "  " + value);
                    hashMap.put(keyName, value);
                } else {
                    try {
                        ArrayList arrayList = (ArrayList) entry.getValue();
                        try {
                            for (int i = 0; i < arrayList.size(); i++) {
                                Deal(keyName, (Map<String, Object>) arrayList.get(i), hashMap);
                            }
                        } catch (ClassCastException e) {
                            hashMap.put(keyName, arrayList);
                        }
                    } catch (ClassCastException e) {
                        //System.out.println("sss");
                    }
                }

            }
        }
        return true;
    }



    public static void main(String[] args) {
        Neo4jDriver neo4jDriver = new Neo4jDriver();
        System.out.println(neo4jDriver.getOneNoderesult("The_New_England_Journal_of_Medicine",1,2));
    }
}
