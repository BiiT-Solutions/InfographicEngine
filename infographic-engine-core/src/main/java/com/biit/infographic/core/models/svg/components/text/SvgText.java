package com.biit.infographic.core.models.svg.components.text;

import com.biit.infographic.core.models.svg.ElementAttributes;
import com.biit.infographic.core.models.svg.ElementType;
import com.biit.infographic.core.models.svg.SvgAreaElement;
import com.biit.infographic.core.models.svg.Unit;
import com.biit.infographic.core.models.svg.exceptions.InvalidAttributeException;
import com.biit.infographic.core.models.svg.serialization.SvgTextDeserializer;
import com.biit.infographic.logger.InfographicEngineLogger;
import com.biit.infographic.logger.SvgGeneratorLogger;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.util.Objects;

@JsonDeserialize(using = SvgTextDeserializer.class)
@JsonRootName(value = "text")
public class SvgText extends SvgAreaElement {
    public static final String NEW_LINE_SYMBOL = "\n";
    public static final int MAX_ITERATIONS = 1000;
    private static final int LINE_SEPARATION = 5;
    private static final int MIN_LINE_SEPARATION = 2;
    private static final int DEFAULT_FONT_SIZE = 10;
    private static final int MINIMUM_FONT_SIZE = 4;
    private static final String DEFAULT_FONT = "sans-serif";
    private static final double MAGIC_INKSCAPE_FONT_Y_CORRECTION = 0.63;

    @JsonProperty("contentText")
    private String text;

    @JsonProperty("fontFamily")
    private String fontFamily = DEFAULT_FONT;

    @JsonProperty("fontSize")
    private int fontSize = DEFAULT_FONT_SIZE;

    //Font calculated to fit the text on a fixed Height
    @JsonIgnore
    private int realFontSize = DEFAULT_FONT_SIZE;

    @JsonIgnore
    private int lineSeparation = LINE_SEPARATION;

    //Font variant must be available on the font.
    @JsonProperty("fontVariant")
    private FontVariantType fontVariant;

    //Rotate uses translate, that rotates not only the element, but also the distance between (0,0) and (x,y)
    @JsonProperty("rotate")
    private long rotate = 0;

    //Needs textLength to work
    @JsonProperty("lengthAdjust")
    private FontLengthAdjust lengthAdjust;

    @JsonProperty("textLength")
    private Long textLength;

    @JsonProperty("textLengthUnit")
    private Unit textLengthUnit = Unit.PIXELS;

    @JsonProperty("dx")
    private Long dx;

    @JsonProperty("dxUnit")
    private Unit dxUnit = Unit.PIXELS;

    @JsonProperty("dy")
    private Long dy;

    @JsonProperty("dyUnit")
    private Unit dyUnit = Unit.PIXELS;

    //In Characters
    @JsonProperty("maxLineLength")
    private Long maxLineLength;

    //In pixels
    @JsonProperty("maxLineWidth")
    private Long maxLineWidth;

    @JsonProperty("maxParagraphHeight")
    private Long maxParagraphHeight;

    @JsonProperty("textAlign")
    private TextAlign textAlign = TextAlign.LEFT;

    @JsonProperty("fontWeight")
    private FontWeight fontWeight;

    @JsonProperty("fontStyle")
    private FontStyle fontStyle;

    public SvgText(ElementAttributes elementAttributes) {
        super(elementAttributes);
        setElementType(ElementType.TEXT);
    }

    public SvgText() {
        this(new ElementAttributes());
        setText("");
    }

    public SvgText(String text, int fontSize, Number x, Number y) {
        this(text, fontSize, x != null ? x.longValue() : null, y != null ? y.longValue() : null);
    }

    public SvgText(String text, int fontSize, Long x, Long y) {
        this(new ElementAttributes(x, y, null, null, null));
        setText(text);
        setFontSize(fontSize);
    }

    public SvgText(String fontFamily, String text, int fontSize, Number x, Number y) {
        this(fontFamily, text, fontSize, x != null ? x.longValue() : null, y != null ? y.longValue() : null);
    }

    public SvgText(String fontFamily, String text, int fontSize, Long x, Long y) {
        this(text, fontSize, x, y);
        setFontFamily(fontFamily);
    }

    public SvgText(String text, int fontSize, String color, Number x, Number y) {
        this(text, fontSize, color, x != null ? x.longValue() : null, y != null ? y.longValue() : null);
    }

    public SvgText(String text, int fontSize, String color, Long x, Long y) {
        this(new ElementAttributes(x, y, null, null, null));
        setText(text);
        setFontSize(fontSize);
        getElementAttributes().setFill(color);
    }

    public SvgText(String text, int fontSize, FontWeight weight, String color, Number x, Number y) {
        this(text, fontSize, weight, color, x != null ? x.longValue() : null, y != null ? y.longValue() : null);
    }

    public SvgText(String text, int fontSize, FontWeight weight, String color, Long x, Long y) {
        this(new ElementAttributes(x, y, null, null, null));
        setText(text);
        setFontSize(fontSize);
        setFontWeight(weight);
        getElementAttributes().setFill(color);
    }

    public String getMainFontFamily() {
        if (fontFamily != null) {
            final String[] fonts = fontFamily.split(",");
            if (fonts.length > 0) {
                return fonts[0].trim();
            }
        }
        return DEFAULT_FONT;
    }


    public String getFontFamily() {
        if (fontFamily != null) {
            return fontFamily;
        }
        return DEFAULT_FONT;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(Integer fontSize) {
        if (fontSize != null) {
            this.fontSize = fontSize;
            this.realFontSize = fontSize;
        } else {
            this.fontSize = DEFAULT_FONT_SIZE;
            this.realFontSize = DEFAULT_FONT_SIZE;
        }
    }


    private int getRealFontSize() {
        return realFontSize;
    }

    private void setRealFontSize(int realFontSize) {
        this.realFontSize = realFontSize;
    }

    private int getLineSeparation() {
        return lineSeparation;
    }

    private void setLineSeparation(int lineSeparation) {
        this.lineSeparation = lineSeparation;
    }

    public FontVariantType getFontVariant() {
        return fontVariant;
    }

    public void setFontVariant(FontVariantType fontVariant) {
        this.fontVariant = fontVariant;
    }

    public FontWeight getFontWeight() {
        if (fontWeight != null) {
            return fontWeight;
        }
        return FontWeight.NORMAL;
    }

    public void setFontWeight(FontWeight fontWeight) {
        this.fontWeight = fontWeight;
    }

    public FontStyle getFontStyle() {
        return fontStyle;
    }

    public void setFontStyle(FontStyle fontStyle) {
        this.fontStyle = fontStyle;
    }

    public long getRotate() {
        return rotate;
    }

    public void setRotate(Long rotate) {
        if (rotate != null) {
            this.rotate = rotate;
        } else {
            this.rotate = 0L;
        }
    }

    public FontLengthAdjust getLengthAdjust() {
        return lengthAdjust;
    }

    public void setLengthAdjust(FontLengthAdjust lengthAdjust) {
        this.lengthAdjust = lengthAdjust;
    }

    public Long getTextLength() {
        return textLength;
    }

    @JsonIgnore
    public String getTextLengthValue() {
        return getTextLength() + getTextLengthUnit().getValue();
    }

    public void setTextLength(String textLength) {
        this.textLength = Unit.getValue(textLength);
        this.textLengthUnit = Unit.getUnit(textLength);
    }

    public void setTextLength(Long textLength) {
        this.textLength = textLength;
    }

    public Unit getTextLengthUnit() {
        return textLengthUnit;
    }

    public void setTextLengthUnit(Unit textLengthUnit) {
        this.textLengthUnit = textLengthUnit;
    }

    public Long getDx() {
        return dx;
    }

    public Unit getDxUnit() {
        return dxUnit;
    }

    public void setDxUnit(Unit dxUnit) {
        this.dxUnit = dxUnit;
    }

    @JsonIgnore
    public String getDxValue() {
        return getDx() + getDxUnit().getValue();
    }

    public void setDx(Long dx) {
        this.dx = dx;
    }

    @JsonIgnore
    public String getDyValue() {
        return getDy() + getDyUnit().getValue();
    }

    public Long getDy() {
        return dy;
    }

    public void setDy(Long dy) {
        this.dy = dy;
    }

    public Unit getDyUnit() {
        return dyUnit;
    }

    public void setDyUnit(Unit dyUnit) {
        this.dyUnit = dyUnit;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    private String addSpacesToNewLines(String text) {
        if (text == null) {
            return null;
        }
        return text.replaceAll("(\n)(?! )", "$1 ");
    }

    public Long getMaxLineLength() {
        return maxLineLength;
    }

    public void setMaxLineLength(Number maxLineLength) {
        if (maxLineLength != null) {
            this.maxLineLength = maxLineLength.longValue();
        } else {
            this.maxLineLength = null;
        }
    }

    public void setMaxLineLength(Long maxLineLength) {
        this.maxLineLength = maxLineLength;
    }

    public TextAlign getTextAlign() {
        return textAlign;
    }

    public void setTextAlign(TextAlign textAlign) {
        this.textAlign = textAlign;
    }

    public Long getMaxLineWidth() {
        return maxLineWidth;
    }

    public void setMaxLineWidth(Number maxLineWidth) {
        if (maxLineWidth != null) {
            this.maxLineWidth = maxLineWidth.longValue();
        } else {
            this.maxLineWidth = null;
        }
    }

    public void setMaxLineWidth(Long maxLineWidth) {
        this.maxLineWidth = maxLineWidth;
    }

    public Long getMaxParagraphHeight() {
        return maxParagraphHeight;
    }

    public void setMaxParagraphHeight(Number maxParagraphHeight) {
        if (maxParagraphHeight != null) {
            this.maxParagraphHeight = maxParagraphHeight.longValue();
        } else {
            this.maxParagraphHeight = null;
        }
    }

    public void setMaxParagraphHeight(Long maxParagraphHeight) {
        this.maxParagraphHeight = maxParagraphHeight;
    }

    @Override
    public Collection<Element> generateSvg(Document doc) {
        final Element text = doc.createElementNS(NAMESPACE, "text");

        List<String> lines;
        int longestLinePixels;
        //To compensate the first size reduction on the loop.
        setLineSeparation(getLineSeparation() + 1);
        do {
            decreaseHeight();
            if (getMaxLineWidth() != null) {
                lines = getLinesByPixels(getText(), getMaxLineWidth());
                longestLinePixels = getMaxLineWidth().intValue();
            } else {
                lines = getLines(getText(), getMaxLineLength() != null ? getMaxLineLength() : Integer.MAX_VALUE);
                final String longestLine = lines.stream().max(Comparator.comparingInt(String::length)).orElse("");
                longestLinePixels = getLineWidthPixels(longestLine);
            }
            //Check text is not overflowing the paragraph.
        } while (!fitsInParagraph(lines) && (getRealFontSize() >= MINIMUM_FONT_SIZE + 1 || getLineSeparation() >= MIN_LINE_SEPARATION));
        if (getRealFontSize() == MINIMUM_FONT_SIZE && getLineSeparation() == MIN_LINE_SEPARATION) {
            SvgGeneratorLogger.warning(this.getClass(), "Text '{}' cannot fit on a height '{}' and width '{}'.",
                    getText(), getMaxParagraphHeight(), getMaxLineWidth() != null ? getMaxLineWidth() : getMaxLineLength());
        }
        int emptyLinesCounter = 0;
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).trim().isEmpty()) {
                emptyLinesCounter++;
                continue;
            }
            final Element elementLine = doc.createElementNS(NAMESPACE, "tspan");
            if (i > 0) {
                elementLine.setAttribute("dy", String.valueOf((getRealFontSize() * (emptyLinesCounter + 1) + getLineSeparation())));
            }
            elementLine.setAttribute("x", String.valueOf(getElementAttributes().getXCoordinate()));
            if (i < lines.size() - 1 && getTextAlign() == TextAlign.JUSTIFY) {
                if (!lines.get(i).endsWith(NEW_LINE_SYMBOL) && !lines.get(i).trim().isEmpty()) {
                    elementLine.setAttribute("letter-spacing", String.valueOf(getLetterSpacing(lines.get(i), longestLinePixels)));
                }
            }
            final String style = generateStyle(null).toString();
            if (!style.isBlank()) {
                elementLine.setAttribute("style", style);
            }
            //Add text without new line character.
            elementLine.setTextContent(lines.get(i).replaceAll(NEW_LINE_SYMBOL, " "));
            text.appendChild(elementLine);
            emptyLinesCounter = 0;
        }

        text.setAttributeNS(null, "x", String.valueOf(getElementAttributes().getXCoordinate()));
        text.setAttributeNS(null, "y", String.valueOf(getElementAttributes().getYCoordinate()
                + getRealFontSize() * MAGIC_INKSCAPE_FONT_Y_CORRECTION));
        if (getFontVariant() != null) {
            text.setAttributeNS(null, "font-variant", getFontVariant().getTag());
        }
        if (getRealFontSize() != 0) {
            text.setAttributeNS(null, "font-size", String.valueOf(getRealFontSize()));
        }
        if (getFontFamily() != null) {
            text.setAttributeNS(null, "font-family", getMainFontFamily());
        }
        if (getFontStyle() != null) {
            text.setAttributeNS(null, "font-style", getFontStyle().getName());
        }
        if (getRotate() != 0) {
            text.setAttributeNS(null, "transform", "rotate(" + getRotate() + ")");
        }
        if (getLengthAdjust() != null) {
            text.setAttributeNS(null, "lengthAdjust", getLengthAdjust().getTag());
        }
        if (getTextLength() != null) {
            text.setAttributeNS(null, "textLength", getTextLengthValue());
        }
        if (getDx() != null) {
            text.setAttributeNS(null, "dx", getDxValue());
        }
        if (getDy() != null) {
            text.setAttributeNS(null, "dy", getDyValue());
        }

        elementStroke(text);
        elementAttributes(text);
        return Collections.singletonList(text);
    }

    @Override
    protected StringBuilder generateStyle(StringBuilder style) {
        style = super.generateStyle(style);
        if (getTextAlign() != null && !getTextAlign().getStyle().isBlank()) {
            style.append(getTextAlign().getStyle());
        }
        if (getFontWeight() != null && getFontWeight() != FontWeight.NORMAL) {
            style.append(getFontWeight().getStyle());
        }
        if (getFontStyle() != null && getFontStyle() != FontStyle.NORMAL) {
            style.append(getFontStyle().getStyle());
        }
        if (getFontFamily() != null) {
            style.append("font-family:");
            style.append(getMainFontFamily());
            style.append(";");
        }

        return style;
    }


    @Override
    public void validateAttributes() throws InvalidAttributeException {
        super.validateAttributes();
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
        if (getTextAlign() == TextAlign.JUSTIFY && (getMaxLineLength() == null && getMaxLineWidth() == null)) {
            throw new InvalidAttributeException(this.getClass(), "Text '" + getId() + "' with property 'justify' needs 'maxLineLength' "
                    + "or 'maxLineWidth' attribute");
        }
        if (getMaxLineLength() != null && getMaxLineWidth() != null) {
            throw new InvalidAttributeException(this.getClass(), "Text '" + getId() + "' can only have 'maxLineLength' or 'maxLineWidth' attribute");
        }
    }

    private int getLineWidthPixels(String text) {
        if (text == null || text.isEmpty()) {
            return 0;
        }
        final AffineTransform affinetransform = new AffineTransform();
        final FontRenderContext frc = new FontRenderContext(affinetransform, true, true);
        final Font font;
        //Load the required font.
        if (FontFactory.getFont(getFontFamily(), getFontWeight()) != null) {
            font = FontFactory.getFont(getFontFamily(), getFontWeight()).deriveFont((float) getRealFontSize());
        } else {
            font = new Font(getMainFontFamily(), Font.PLAIN, getRealFontSize());
        }
        return (int) (font.getStringBounds(text, frc).getWidth());
    }

    private boolean fitsInParagraph(List<String> lines) {
        if (getMaxParagraphHeight() == null) {
            return true;
        }
        final int textHeight = (getRealFontSize() + getLineSeparation()) * lines.size();
        return getMaxParagraphHeight() >= textHeight;
    }

    private void decreaseHeight() {
        if (getLineSeparation() > MIN_LINE_SEPARATION) {
            setLineSeparation(getLineSeparation() - 1);
        } else {
            setRealFontSize(getRealFontSize() - 1);
            setLineSeparation(LINE_SEPARATION);
        }
    }

    private double getLetterSpacing(String text, int maxLinePixels) {
        final int currentPixels = getLineWidthPixels(text);
        return (double) (maxLinePixels - currentPixels) / text.length();
    }

    private List<String> getLines(String content, long maxLineLength) {
        final List<String> lines = new ArrayList<>();
        if (content != null) {
            int iterations = 0;
            while (!content.isBlank() && iterations < MAX_ITERATIONS) {
                iterations++;
                if (content.startsWith(NEW_LINE_SYMBOL)) {
                    lines.add("");
                    content = content.substring(NEW_LINE_SYMBOL.length() + 1);
                    continue;
                }
                final int endOfLine;
                //New Line forced by user.
                if (content.indexOf(NEW_LINE_SYMBOL) > 0 && content.indexOf(NEW_LINE_SYMBOL) < maxLineLength) {
                    endOfLine = content.indexOf(NEW_LINE_SYMBOL) + NEW_LINE_SYMBOL.length();
                } else {
                    endOfLine = (int) maxLineLength;
                }
                if (content.length() < endOfLine) {
                    lines.add(content);
                    break;
                }
                final String pieceOfText = content.substring(0, endOfLine);
                for (int i = pieceOfText.length() - 1; i >= 0; i--) {
                    if (Character.isWhitespace(pieceOfText.charAt(i))) {
                        lines.add(pieceOfText.substring(0, i));
                        //Add new line separator for later
                        if (Objects.equals(pieceOfText.charAt(i) + "", NEW_LINE_SYMBOL)) {
                            lines.set(lines.size() - 1, lines.get(lines.size() - 1) + NEW_LINE_SYMBOL);
                        }
                        try {
                            content = content.substring(i + 1);
                        } catch (IndexOutOfBoundsException e) {
                            content = "";
                        }
                        break;
                    }
                }
            }
            if (iterations >= MAX_ITERATIONS) {
                InfographicEngineLogger.warning(this.getClass(), "Text '{}' lines not calculated properly!", content);
            }
        }
        return lines;
    }

    private List<String> getLinesByPixels(String content, long lineWidth) {
        final List<String> lines = new ArrayList<>();
        if (content != null) {
            content = addSpacesToNewLines(content);
            final Deque<String> words = new ArrayDeque<>(Arrays.asList(content.split("[ \\t\\x0B\\f\\r]+")));
            if (words.size() == 1) {
                lines.add(words.pop());
                return lines;
            }
            int iterator = 0;
            while (!words.isEmpty() && iterator < lineWidth) {
                iterator++;
                final StringBuilder lineToCheck = new StringBuilder();
                String line = "";
                long extraLines = 0;
                while (true) {
                    if (!lineToCheck.isEmpty()) {
                        lineToCheck.append(" ");
                    }
                    if (words.peek() == null) {
                        break;
                    }

                    lineToCheck.append(words.peek());
                    if (getLineWidthPixels(lineToCheck.toString()) < lineWidth) {
                        final String word = words.peek();
                        //New Line forced by user.
                        if (word != null && word.endsWith(NEW_LINE_SYMBOL)) {
                            line = lineToCheck.toString();
                            words.pop();
                            //Add extra lines if multiple line symbols appear.
                            extraLines = Math.max(0, StringUtils.countMatches(word, NEW_LINE_SYMBOL) - 1);
                            break;
                        } else {
                            line = lineToCheck.toString();
                            words.pop();
                        }
                    } else {
                        break;
                    }
                }
//                if (!line.isEmpty()) {
                try {
                    lines.add(line);
                } catch (OutOfMemoryError r) {
                    InfographicEngineLogger.severe(this.getClass(), "Cannot add line '" + line
                            + "'.");
                    InfographicEngineLogger.errorMessage(this.getClass(), r);
                }
//                }
                for (int i = 0; i < extraLines; i++) {
                    lines.add(" ");
                }
            }
            if (iterator >= lineWidth) {
                InfographicEngineLogger.warning(this.getClass(), "Text '{}' length not calculated properly!", content);
            }
        }
        return lines;
    }

    public boolean mustEmbedFont() {
        final String mainFont = getMainFontFamily();
        //We only embed the first font choice.
        if (FontFactory.getFont(mainFont, getFontWeight()) == null) {
            SvgGeneratorLogger.warning(this.getClass(), "Font '{}' '{}' is not embeddable.", mainFont, getFontWeight());
        }
        return FontFactory.getFont(mainFont, getFontWeight()) != null;
    }

    public Element embeddedFont(Document doc) {
        //We only embed the first font choice.
        final String mainFont = getMainFontFamily();
        SvgGeneratorLogger.debug(this.getClass(), "Embedding font '{}'.", mainFont);
        final Element style = doc.createElementNS(NAMESPACE, "style");
        style.setAttributeNS(null, "type", "text/css");
        try {
            style.setTextContent(embeddedFontScript(mainFont));
        } catch (IOException e) {
            SvgGeneratorLogger.severe(this.getClass(), "Cannot embed font '{}'.", mainFont);
            SvgGeneratorLogger.errorMessage(this.getClass(), e);
            return null;
        }
        return style;
    }

    private String embeddedFontScript(String fontFamily) throws IOException {
        final StringBuilder script = new StringBuilder();
//        script.append("<![CDATA[\n");
        script.append("\n\t\t@font-face {\n");
        script.append("\t\t\tfont-family: '").append(fontFamily).append("';\n");
//        if (getFontWeight() != null && getFontWeight() != FontWeight.NORMAL) {
        script.append("\t\t\t").append(getFontWeight().getFontDefinition()).append("\n");
//        }
        script.append("\t\t\tsrc: url('data:application/font-truetype;charset=utf-8;base64,")
                .append(FontFactory.encodeFontToBase64(fontFamily, getFontWeight())).append("');\n");
        script.append("\t\t}\n");
//        script.append("]]>\n");
        return script.toString();
    }

}
