package jaxb.unmarshal;

import jaxb.schema.generated.PRDWorld;
import jaxb.unmarshal.converter.impl.PRDConverter;
import objects.world.World;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * A class designated to read the XML file data and convert it to program objects we can work with.
 */
public class Reader {


    /**
     * Given an XML file, reads the world settings in it and returns the world object.
     * Reads the XML world into an intermediate class from the given schema and converts the intermediate Object to an object used in the program.
     *
     * @param filePath The xml file path to read the world from.
     * @return The world read from the xml file
     */
    public static World readWorldFromXML(String filePath) {
        try {
            InputStream inputStream = new FileInputStream(new File(filePath));
            PRDWorld prdWorld = deserializeFrom(inputStream);
            return PRDConverter.PRDWorld2World(prdWorld);
        } catch (FileNotFoundException | JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Given the XML setting file as an input stream, reads it into the PRDWorld class generated from the scheme.
     *
     * @param in the given input stream (in this case the XML settings file)
     * @return the PRDWorld read from the XML setting file.
     * @throws JAXBException
     */
    private static PRDWorld deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(jaxb.schema.generated.PRDWorld.class);
        Unmarshaller u = jc.createUnmarshaller();
        return (PRDWorld) u.unmarshal(in);
    }

}
