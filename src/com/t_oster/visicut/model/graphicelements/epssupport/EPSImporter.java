package com.t_oster.visicut.model.graphicelements.epssupport;

import com.t_oster.visicut.misc.ExtensionFilter;
import com.t_oster.visicut.model.graphicelements.GraphicSet;
import com.t_oster.visicut.model.graphicelements.ImportException;
import com.t_oster.visicut.model.graphicelements.Importer;
import com.t_oster.visicut.model.graphicelements.svgsupport.SVGImporter;
import java.awt.Dimension;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.filechooser.FileFilter;
import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.freehep.postscript.PSInputFile;
import org.freehep.postscript.Processor;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

/**
 *
 * @author thommy
 */
public class EPSImporter implements Importer
{

  public FileFilter getFileFilter()
  {
    return new ExtensionFilter(".eps", "Encapsulated PostScript (*.eps)");
  }

  public GraphicSet importFile(File inputFile) throws ImportException
  {
    Writer out = null;
    try
    {
      // Get a DOMImplementation
      DOMImplementation domImpl =
        GenericDOMImplementation.getDOMImplementation();
      // Create an instance of org.w3c.dom.Document
      Document document = domImpl.createDocument(null, "svg", null);
      // Create an instance of the SVG Generator
      final SVGGraphics2D svgGenerator = new SVGGraphics2D(document);
      svgGenerator.setTransform(new AffineTransform());
      // Open input file
      PSInputFile in = new PSInputFile(inputFile.getAbsolutePath());
      Dimension d = new Dimension(800, 600);
      // Create processor and associate to input and output file
      Processor processor = new Processor(svgGenerator, d, false);
      processor.setData(in);

      // Process
      processor.process();
      File tmp = File.createTempFile("temp", "svg");
      tmp.deleteOnExit();
      svgGenerator.stream(new FileWriter(tmp));
      return new SVGImporter().importFile(tmp);
    }
    catch (Exception ex)
    {
      Logger.getLogger(EPSImporter.class.getName()).log(Level.SEVERE, null, ex);
      throw new ImportException(ex);
    }
  }
}