# Objective

This software execute templates in JSON format and covert them into SVG images.

# Standard Components

This is a list of the implemented components and the properties that are available for each of them. For some code
example, please check the test inside `infographic-engine-core` module.

### Circle

**ElementAttributes.id** The id of the element.

**ElementAttributes.class** The class of the element. Useful when combined with CSS.

**ElementAttributes.x** The position of the center of the circle.

**ElementAttributes.y** The position of the center of the circle.

**ElementAttributes.fill** The color of the circle.

**ElementAttributes.style** Svg Style definition as string that will be injected on the component.

**ElementStroke.strokeLinecap** The ending shape of the line as `round`, `butt` or `square`.

**ElementStroke.strokeWidth** The width of the stroke to be applied to the shape.

**ElementStroke.strokeColor** The color of the stroke to be applied to the shape.

**ElementStroke.strokeDash** Defines the pattern of dashes and gaps used to paint the outline of the shape. It is
defined as an array
of integers where each integer is the length of one dash.

### Ellipse

**ElementAttributes.id** The id of the element.

**ElementAttributes.class** The class of the element. Useful when combined with CSS.

**ElementAttributes.x** The position of the center of the ellipse.

**ElementAttributes.y** The position of the center of the ellipse.

**ElementAttributes.width** The width of the element.

**ElementAttributes.widthUnit** The unit applied on `ElementAttributes.width`. Available values are `%` and `px`.

**ElementAttributes.height** Starting point of the text on the y-axis.

**ElementAttributes.heightUnit** The unit applied on `ElementAttributes.height`. Available values are `%` and `px`.

**ElementAttributes.fill** The color of the ellipse.

**ElementAttributes.style** Svg Style definition as string that will be injected on the component.

**ElementStroke.strokeLinecap** The ending shape of the line as `round`, `butt` or `square`.

**ElementStroke.strokeWidth** The width of the stroke to be applied to the shape.

**ElementStroke.strokeColor** The color of the stroke to be applied to the shape.

**ElementStroke.strokeDash** Defines the pattern of dashes and gaps used to paint the outline of the shape. It is
defined as an array
of integers where each integer is the length of one dash.

### Image

**content** The image as base64.

**href** A link if needed.

**ElementAttributes.id** The id of the element.

**ElementAttributes.class** The class of the element. Useful when combined with CSS.

**ElementAttributes.x** Starting point of the image on the x-axis in pixels.

**ElementAttributes.y** Starting point of the image on the y-axis in pixels.

### Line

**x2Coordinate** Ending point of the line on the x-axis in pixels.

**y2Coordinate** Ending point of the line on the y-axis in pixels.

**ElementAttributes.x** Starting point of the line on the x-axis in pixels.

**ElementAttributes.y** Starting point of the line on the y-axis in pixels.

**ElementAttributes.fill** The color of the line.

**ElementAttributes.style** Svg Style definition as string that will be injected on the component.

**ElementStroke.strokeLinecap** The ending shape of the line as `round`, `butt` or `square`.

**ElementStroke.strokeWidth** The width of the stroke to be applied to the shape.

**ElementStroke.strokeColor** The color of the stroke to be applied to the shape.

**ElementStroke.strokeDash** Defines the pattern of dashes and gaps used to paint the outline of the shape. It is
defined as an array
of integers where each integer is the length of one dash.

### Rectangle

**xRadius** To define rounded corners on x Axis.

**yRadius** To define rounded corners on y Axis.

**ElementAttributes.id** The id of the element.

**ElementAttributes.class** The class of the element. Useful when combined with CSS.

**ElementAttributes.x** Starting point of the rectangle on the x-axis in pixels.

**ElementAttributes.y** Starting point of the rectangle on the y-axis in pixels.

**ElementAttributes.width** The width of the element.

**ElementAttributes.widthUnit** The unit applied on `ElementAttributes.width`. Available values are `%` and `px`.

**ElementAttributes.height** Starting point of the text on the y-axis.

**ElementAttributes.heightUnit** The unit applied on `ElementAttributes.height`. Available values are `%` and `px`.

**ElementAttributes.fill** The color of the rectangle.

**ElementAttributes.style** Svg Style definition as string that will be injected on the component.

**ElementAttributes.verticalAlign**

**ElementStroke.strokeLinecap** The ending shape of the line as `round`, `butt` or `square`.

**ElementStroke.strokeWidth** The width of the stroke to be applied to the shape.

**ElementStroke.strokeColor** The color of the stroke to be applied to the shape.

**ElementStroke.strokeDash** Defines the pattern of dashes and gaps used to paint the outline of the shape. It is
defined as an array
of integers where each integer is the length of one dash.

### Script

Used in combination with other components, allows to include some basic javascript in the image to make it interactive.

**script** The javascript that will be available on the image.

### Text

Sets a text in the image.

The allowed properties are:

**contentText** The text to be shown.

**fontFamily** The family font to be used to display the text. By default, is 'Sans-Serif'.

**fontSize** The size of the font.

**fontVariant** The Variant of the font. Possible values
are `normal`, `none`, `small-caps`, `all-small-caps`, `titling-caps`, `petite-caps`, `all-petite-caps`, `unicase`, `slashed-zero`.
Note that the font type must have the desired variant implemented or will be ignored.

**rotate** Rotates the text. Note that the distance between the text and the axis is also rotated, therefore the text
can be also translated.

**maxLineLength** Maximum length of a line in characters number. Do not split complete words and distribute the text on
several lines if needed.

**maxLineWidth** Maximum length of a line in pixels. Do not split complete words and distribute the text on
several lines if needed.

**textAlign** Alignment of a text in a paragraph. Must be combined with `maxLineLength` or `maxLineWidth`. Allowed
values are `left`, `right`, `center`, `justify`.

**textLength** Forces a length on the text.

**textLengthUnit** The unit applied on `textLength`. Available values are `%` and `px`.

**lengthAdjust** How the text is adapted to the use of `textLength`. Values are `spacing`and `spacingAndGlyphs`.

**dx** Indicates a shift along the x-axis on the position of an element or its content.

**dy** Indicates a shift along the y-axis on the position of an element or its content.

**ElementAttributes.id** The id of the element.

**ElementAttributes.class** The class of the element. Useful when combined with CSS.

**ElementAttributes.x** Starting point of the text on the x-axis.

**ElementAttributes.y** Starting point of the text on the y-axis.

**ElementAttributes.fill** The color of the text.

**ElementStroke.strokeLinecap** The ending shape of the line as `round`, `butt` or `square`.

**ElementStroke.strokeWidth** The width of the stroke to be applied to the shape.

**ElementStroke.strokeColor** The color of the stroke to be applied to the shape.

**ElementStroke.strokeDash** Defines the pattern of dashes and gaps used to paint the outline of the shape. It is
defined as an array
of integers where each integer is the length of one dash.

# Custom Components

### Gauge

**min** Minimum value on the Gauge.

**max** Maximum value on the Gauge.

**value** Current value on the Gauge. Where the arrow is pointing to.

**type** The type of the gauge. Currently we have 'gradient' and 'five_values'.

**colors** If you want to override the default colors of the gauge. Can have 5 elements.

**ElementAttributes.id** The id of the element.

**ElementAttributes.class** The class of the element. Useful when combined with CSS.

**ElementAttributes.x** Starting point of the text on the x-axis.

**ElementAttributes.y** Starting point of the text on the y-axis.
