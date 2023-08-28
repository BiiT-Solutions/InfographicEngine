package com.biit.infographic.core.models.svg.components.text;

import com.biit.infographic.core.models.svg.ElementAttributes;
import com.biit.infographic.core.models.svg.ElementType;
import com.biit.infographic.core.models.svg.SvgElement;
import com.biit.infographic.core.models.svg.exceptions.InvalidAttributeException;
import com.biit.infographic.core.models.svg.utils.Color;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

@JsonRootName(value = "text")
public class SvgText extends SvgElement {
    private static final int LINE_SEPARATION = 5;

    @JsonProperty("contentText")
    private String text;

    @JsonProperty("font-family")
    private String fontFamily = "sans-serif";

    @JsonProperty("font-size")
    private int fontSize = 0;

    //Font variant must be available on the font.
    @JsonProperty("font-variant")
    private FontVariantType fontVariant;

    //Rotate uses translate, that rotates not only the element, but also the distance between (0,0) and (x,y)
    @JsonProperty("rotate")
    private long rotate = 0;

    //Needs textLength to work
    @JsonProperty("lengthAdjust")
    private FontLengthAdjust lengthAdjust;

    @JsonProperty("textLength")
    private String textLength;

    @JsonProperty("dx")
    private String dx;

    @JsonProperty("dy")
    private String dy;

    //In Characters
    @JsonProperty("maxLineSize")
    private Integer maxLineLength;

    //In pixels
    @JsonProperty("maxLineSize")
    private Integer lineWidth;

    @JsonProperty("text-align")
    private TextAlign textAlign = TextAlign.LEFT;

    @JsonProperty("stroke-width")
    private Double strokeWidth;

    @JsonProperty("stroke")
    private String strokeColor;

    @JsonProperty("stroke-dasharray")
    private List<Integer> strokeDash;

    public SvgText(ElementAttributes elementAttributes) {
        super(elementAttributes);
        setElementType(ElementType.TEXT);
    }

    public SvgText() {
        this(new ElementAttributes());
        setText("");
    }

    public SvgText(String text, int fontSize, Long x, Long y) {
        this(new ElementAttributes(x, y, null, null, null));
        setText(text);
        setFontSize(fontSize);
    }

    public SvgText(String text, int fontSize, Long x, Long y, Integer lineWidth) {
        this(new ElementAttributes(x, y, null, null, null));
        setText(text);
        setFontSize(fontSize);
        setLineWidth(lineWidth);
    }


    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public FontVariantType getFontVariant() {
        return fontVariant;
    }

    public void setFontVariant(FontVariantType fontVariant) {
        this.fontVariant = fontVariant;
    }

    public long getRotate() {
        return rotate;
    }

    public void setRotate(long rotate) {
        this.rotate = rotate;
    }

    public FontLengthAdjust getLengthAdjust() {
        return lengthAdjust;
    }

    public void setLengthAdjust(FontLengthAdjust lengthAdjust) {
        this.lengthAdjust = lengthAdjust;
    }

    public String getTextLength() {
        return textLength;
    }

    public void setTextLength(String textLength) {
        this.textLength = textLength;
    }

    public String getDx() {
        return dx;
    }

    public void setDx(String dx) {
        this.dx = dx;
    }

    public String getDy() {
        return dy;
    }

    public void setDy(String dy) {
        this.dy = dy;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getMaxLineLength() {
        return maxLineLength;
    }

    public void setMaxLineLength(Integer maxLineLength) {
        this.maxLineLength = maxLineLength;
    }

    public TextAlign getTextAlign() {
        return textAlign;
    }

    public void setTextAlign(TextAlign textAlign) {
        this.textAlign = textAlign;
    }

    public String getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeColor(String strokeColor) {
        this.strokeColor = Color.checkColor(strokeColor);
    }

    public Double getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(Double strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public List<Integer> getStrokeDash() {
        return strokeDash;
    }

    public void setStrokeDash(List<Integer> strokeDash) {
        this.strokeDash = strokeDash;
    }

    public Integer getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(Integer lineWidth) {
        this.lineWidth = lineWidth;
    }

    @Override
    public Element generateSvg(Document doc) {
        final Element text = doc.createElementNS(NAMESPACE, "text");
        text.setAttributeNS(null, "x", String.valueOf(getElementAttributes().getXCoordinate()));
        text.setAttributeNS(null, "y", String.valueOf(getElementAttributes().getYCoordinate()));
        if (getFontVariant() != null) {
            text.setAttributeNS(null, "font-variant", getFontVariant().getTag());
        }
        if (getFontSize() != 0) {
            text.setAttributeNS(null, "font-size", String.valueOf(getFontSize()));
        }
        if (getFontFamily() != null) {
            text.setAttributeNS(null, "font-family", getFontFamily());
        }
        if (getRotate() != 0) {
            text.setAttributeNS(null, "transform", "rotate(" + getRotate() + ")");
        }
        if (getLengthAdjust() != null) {
            text.setAttributeNS(null, "lengthAdjust", getLengthAdjust().getTag());
        }
        if (getTextLength() != null) {
            text.setAttributeNS(null, "textLength", getTextLength());
        }
        if (getDx() != null) {
            text.setAttributeNS(null, "dx", getDx());
        }
        if (getDy() != null) {
            text.setAttributeNS(null, "dy", getDy());
        }
        text.setAttributeNS(null, "stroke", getStrokeColor());
        text.setAttributeNS(null, "stroke-width", String.valueOf(getStrokeWidth()));
        if (strokeDash != null && !strokeDash.isEmpty()) {
            text.setAttributeNS(null, "stroke-dasharray", strokeDash.stream().map(String::valueOf)
                    .collect(Collectors.joining(" ")));
        }

        if (getLineWidth() != null || getMaxLineLength() != null) {
            final List<String> lines;
            final int longestLinePixels;
            if (getLineWidth() != null) {
                lines = getLinesByPixels(getText(), getLineWidth());
                longestLinePixels = getLineWidth();
            } else {
                lines = getLines(getText(), getMaxLineLength());
                final String longestLine = lines.stream().max(Comparator.comparingInt(String::length)).orElse("");
                longestLinePixels = getLinePixels(longestLine);
            }
            for (int i = 0; i < lines.size(); i++) {
                final Element elementLine = doc.createElementNS(NAMESPACE, "tspan");
                elementLine.setAttribute("dy", String.valueOf(getFontSize() + LINE_SEPARATION));
                elementLine.setAttribute("x", String.valueOf(getElementAttributes().getXCoordinate()));
                if (i < lines.size() - 1 && getTextAlign() == TextAlign.JUSTIFY) {
                    elementLine.setAttribute("letter-spacing", String.valueOf(getLetterSpacing(lines.get(i), longestLinePixels)));
                }
                elementLine.setAttribute("style", getTextAlign().getStyle());
                elementLine.setTextContent(lines.get(i));
                text.appendChild(elementLine);
            }
        } else {
            text.setTextContent(getText());
        }

        if (getTextAlign() != null && !getTextAlign().getStyle().isBlank()) {
            getElementAttributes().addStyle("text-align:" + getTextAlign().getStyle());
        }

        elementAttributes(text);
        return text;
    }


    @Override
    public void validateAttributes() throws InvalidAttributeException {
        if (getElementAttributes().getWidth() != null) {
            throw new InvalidAttributeException(this.getClass(), "Text '" + getId() + "' must not have 'width' attribute");
        }
        if (getElementAttributes().getHeight() != null) {
            throw new InvalidAttributeException(this.getClass(), "Text '" + getId() + "' must not have 'height' attribute");
        }
        if (getText() == null) {
            throw new InvalidAttributeException(this.getClass(), "Text '" + getId() + "' does not have 'text' attribute");
        }
        if (getFontSize() == 0) {
            throw new InvalidAttributeException(this.getClass(), "Text '" + getId() + "' does not have 'fontSize' attribute");
        }
        if (getTextAlign() == TextAlign.JUSTIFY && getMaxLineLength() == null) {
            throw new InvalidAttributeException(this.getClass(), "Text '" + getId() + "' with property 'justify' needs 'maxLineLength' attribute");
        }
    }

    private int getLinePixels(String text) {
        final AffineTransform affinetransform = new AffineTransform();
        final FontRenderContext frc = new FontRenderContext(affinetransform, true, true);
        final Font font = new Font(getFontFamily().split(",")[0].trim(), Font.PLAIN, 12);
        final int textwidth = (int) (font.getStringBounds(text, frc).getWidth());
        //final int textheight = (int) (font.getStringBounds(text, frc).getHeight());
        return textwidth;
    }

    private double getLetterSpacing(String text, int maxLinePixels) {
        final int currentPixels = getLinePixels(text);
        return (double) (maxLinePixels - currentPixels) / text.length();
    }

    private List<String> getLines(String content, int maxLineLength) {
        final List<String> lines = new ArrayList<>();
        if (content != null) {
            while (!content.isBlank()) {
                if (content.length() < maxLineLength) {
                    lines.add(content);
                    break;
                }
                final String pieceOfText = content.substring(0, maxLineLength);
                for (int i = pieceOfText.length() - 1; i >= 0; i--) {
                    if (Character.isWhitespace(pieceOfText.charAt(i))) {
                        lines.add(pieceOfText.substring(0, i));
                        try {
                            content = content.substring(i + 1);
                        } catch (IndexOutOfBoundsException e) {
                            content = "";
                        }
                        break;
                    }
                }
            }
        }
        return lines;
    }

    private List<String> getLinesByPixels(String content, int lineWidth) {
        final List<String> lines = new ArrayList<>();
        if (content != null) {
            final Deque<String> words = new ArrayDeque<>(Arrays.asList(content.split("\\s+")));
            while (!words.isEmpty()) {
                final StringBuilder lineToCheck = new StringBuilder();
                String line = "";
                while (true) {
                    if (!lineToCheck.isEmpty()) {
                        lineToCheck.append(" ");
                    }
                    if (words.peek() == null) {
                        break;
                    }
                    lineToCheck.append(words.peek());
                    if (getLinePixels(lineToCheck.toString()) < lineWidth) {
                        line = lineToCheck.toString();
                        words.pop();
                    } else {
                        break;
                    }
                }
                if (!line.isEmpty()) {
                    lines.add(line);
                }
            }

        }
        return lines;
    }
}
