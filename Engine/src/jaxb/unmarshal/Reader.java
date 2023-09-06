package jaxb.unmarshal;

import jaxb.schema.generated.PRDWorld;
import jaxb.unmarshal.converter.PRDConverter;
import simulation.objects.world.World;

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
     * @param file The xml file to read the world from.
     * @return The world read from the xml file
     */
    public static World readWorldFromXML(File file) {
        PRDConverter prdConverter = new PRDConverter();
        try {
            InputStream inputStream = new FileInputStream(file);
            PRDWorld prdWorld = deserializeFrom(inputStream);
            return prdConverter.PRDWorld2World(prdWorld);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("The given path is invalid. Please provide the full path for the simulation config file.");
        } catch (JAXBException e) {
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

    /**
     * validates that the file exists and that is an .xml file, otherwise throws exception.
     * @param path a path to a world configuration xml file.
     */
    public static void validatePath(String path) {
        File filePath = new File(path);

        if(!filePath.exists())
        {
            throw new IllegalArgumentException("The path provided is not valid. Please provide the full path to the simulation config file.");
        }
        if(!path.endsWith(".xml")){
            throw new IllegalArgumentException("The file provided is not valid. Please provide the path to a valid simulation config file  with the .xml format.");
        }
    }

}
