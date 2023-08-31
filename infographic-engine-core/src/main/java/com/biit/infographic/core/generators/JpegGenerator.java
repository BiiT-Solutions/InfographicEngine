package com.biit.infographic.core.generators;

import com.biit.infographic.core.models.svg.ISvgElement;
import com.biit.infographic.core.models.svg.SvgTemplate;
import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public final class JpegGenerator {
    private static final float JPG_QUALITY = 0.8F;
    private static final float EIGHT_K = 8192;

    private JpegGenerator() {

    }

    public static byte[] generate(SvgTemplate svgTemplate) {
        final DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
        final Document doc = impl.createDocument(ISvgElement.NAMESPACE, "svg", null);

        svgTemplate.generateSvg(doc);

        return convertToByteArray(svgTemplate, doc);
    }

    public static byte[] convertToByteArray(SvgTemplate svgTemplate, Document doc) {
        final JPEGTranscoder jpegTranscoder = new JPEGTranscoder();

        if (svgTemplate.getSvgBackground() != null && svgTemplate.getSvgBackground().getBackgroundColor() != null) {
            jpegTranscoder.addTranscodingHint(ImageTranscoder.KEY_BACKGROUND_COLOR, Color.decode(svgTemplate.getSvgBackground().getBackgroundColor()));
        }

        // Set the transcoding hints.
        jpegTranscoder.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, JPG_QUALITY);

        final TranscoderInput input = new TranscoderInput(doc);
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
            final TranscoderOutput output = new TranscoderOutput(stream);
            jpegTranscoder.transcode(input, output);
            return stream.toByteArray();
        } catch (IOException | TranscoderException e) {
            throw new RuntimeException(e);
        }
    }


}
