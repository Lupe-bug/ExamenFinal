/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AG;

/**
 *
 * @author barah
 */
import com.sun.prism.impl.Disposer.Record;
import java.util.ArrayList;
import java.util.List;
import javax.naming.spi.DirStateFactory.Result;
import org.neo4j.driver.Values;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.TransactionWork;
import org.neo4j.driver.exceptions.ClientException;
import static org.neo4j.driver.Values.parameters;


public class AgendaTelefonica {
    
private final Driver driver;
private final String uri;
private final String username;
private final String password;
    

public AgendaTelefonica(String uri, String username, String password) {
    this.uri = "http://localhost:7474/browser/";
    this.username = "neo4j";
    this.password = "GuadalupeUMG";
    this.driver = GraphDatabase.driver(uri, AuthTokens.basic(username, password));
}

public void close() {
    driver.close();
}


public void agregarContacto(String nombre, String telefono) {
    try (Session session = driver.session()) {
        session.writeTransaction((Transaction tx) -> {
            tx.run("CREATE (:Contacto {nombre: $nombre, telefono: $telefono})",
                    Values.parameters("nombre", nombre, "telefono", telefono));
            
            return null;
        });
    } catch (ClientException e) {
        System.err.println("Error al agregar el contacto: " + e.getMessage());
    }
}


public void crearRelacion(String nombre1, String nombre2, String relacion) {
    try (Session session = driver.session()) {
        session.writeTransaction((Transaction tx) -> {
            tx.run("MATCH (c1:Contacto {nombre: $nombre1}), (c2:Contacto {nombre: $nombre2}) " +
                    "CREATE (c1)-[:CONOCE_A]->(c2)",
                    Values.parameters("nombre1", nombre1, "nombre2", nombre2));
            return null;
        });
    } catch (ClientException e) {
        System.err.println("Error al crear la relación: " + e.getMessage());
    }
}


/*public String buscarContacto(String nombre) {
try (Session session = driver.session()) {
return session.readTransaction((Transaction tx) -> {
Result result = (Result) tx.run("MATCH (c:Contacto {nombre: $nombre}) RETURN c.telefono",
parameters("nombre", nombre));
if (result.hasNext()) {
Record record = result.next();
return record.get("c.telefono").asString();


} else {
return "No se encontró el contacto";
}
});
} catch (ClientException e) {
System.err.println("Error al buscar el contacto: " + e.getMessage());
return "Error al buscar el contacto";
}*/


/*public List<String> obtenerRelaciones(String nombre) {
try (Session session = driver.session()) {
return session.readTransaction((Transaction tx) -> {
Result result = (Result) tx.run("RETURN c2.nombre" + "MATCH (c1:Contacto {nombre: $nombre})-[:CONOCE_A]->(c2) ", parameters("nombre", nombre));
List<String> relaciones = new ArrayList<>();
while (result.hasNext()) {
Record record = result.next();
relaciones.add(record.get("c2.nombre").asString());

}
return relaciones;
});
} catch (ClientException e) {
System.err.println("Error al obtener las relaciones: " + e.getMessage());
return new ArrayList<>();
}
}*/



}
