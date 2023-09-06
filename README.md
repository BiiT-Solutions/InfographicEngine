# Objective

This software execute templates in JSON format and covert them into SVG images.

# Standard Components

This is a list of the implemented components and the properties that are available for each of them. For some code
example, please check the test inside `infographic-engine-core` module.

### Circle

**commonAttributes.id** The id of the element.

**commonAttributes.class** The class of the element. Useful when combined with CSS.

**commonAttributes.x** The position of the center of the circle.

**commonAttributes.y** The position of the center of the circle.

**commonAttributes.fill** The color of the circle.

**commonAttributes.style** Svg Style definition as string that will be injected on the component.

**stroke.strokeLinecap** The ending shape of the line as `round`, `butt` or `square`.

**stroke.strokeWidth** The width of the stroke to be applied to the shape.

**stroke.strokeColor** The color of the stroke to be applied to the shape.

**stroke.strokeDash** Defines the pattern of dashes and gaps used to paint the outline of the shape. It is
defined as an array
of integers where each integer is the length of one dash.

### Ellipse

**commonAttributes.id** The id of the element.

**commonAttributes.class** The class of the element. Useful when combined with CSS.

**commonAttributes.x** The position of the center of the ellipse.

**commonAttributes.y** The position of the center of the ellipse.

**commonAttributes.width** The width of the element.

**commonAttributes.widthUnit** The unit applied on `commonAttributes.width`. Available values are `%` and `px`.

**commonAttributes.height** Starting point of the text on the y-axis.

**commonAttributes.heightUnit** The unit applied on `commonAttributes.height`. Available values are `%` and `px`.

**commonAttributes.fill** The color of the ellipse.

**commonAttributes.style** Svg Style definition as string that will be injected on the component.

**stroke.strokeLinecap** The ending shape of the line as `round`, `butt` or `square`.

**stroke.strokeWidth** The width of the stroke to be applied to the shape.

**stroke.strokeColor** The color of the stroke to be applied to the shape.

**stroke.strokeDash** Defines the pattern of dashes and gaps used to paint the outline of the shape. It is
defined as an array of integers where each integer is the length of one dash.

### Image

**content** The image as base64.

**href** A link if needed.

**commonAttributes.id** The id of the element.

**commonAttributes.class** The class of the element. Useful when combined with CSS.

**commonAttributes.x** Starting point of the image on the x-axis in pixels.

**commonAttributes.y** Starting point of the image on the y-axis in pixels.

### Line

**x2Coordinate** Ending point of the line on the x-axis in pixels.

**y2Coordinate** Ending point of the line on the y-axis in pixels.

**commonAttributes.x** Starting point of the line on the x-axis in pixels.

**commonAttributes.y** Starting point of the line on the y-axis in pixels.

**commonAttributes.fill** The color of the line.

**commonAttributes.style** Svg Style definition as string that will be injected on the component.

**stroke.strokeLinecap** The ending shape of the line as `round`, `butt` or `square`.

**stroke.strokeWidth** The width of the stroke to be applied to the shape.

**stroke.strokeColor** The color of the stroke to be applied to the shape.

**stroke.strokeDash** Defines the pattern of dashes and gaps used to paint the outline of the shape. It is
defined as an array
of integers where each integer is the length of one dash.

### Rectangle

**xRadius** To define rounded corners on x Axis.

**yRadius** To define rounded corners on y Axis.

**commonAttributes.id** The id of the element.

**commonAttributes.class** The class of the element. Useful when combined with CSS.

**commonAttributes.x** Starting point of the rectangle on the x-axis in pixels.

**commonAttributes.y** Starting point of the rectangle on the y-axis in pixels.

**commonAttributes.width** The width of the element.

**commonAttributes.widthUnit** The unit applied on `commonAttributes.width`. Available values are `%` and `px`.

**commonAttributes.height** Starting point of the text on the y-axis.

**commonAttributes.heightUnit** The unit applied on `commonAttributes.height`. Available values are `%` and `px`.

**commonAttributes.fill** The color of the rectangle.

**commonAttributes.style** Svg Style definition as string that will be injected on the component.

**commonAttributes.verticalAlign**

**stroke.strokeLinecap** The ending shape of the line as `round`, `butt` or `square`.

**stroke.strokeWidth** The width of the stroke to be applied to the shape.

**stroke.strokeColor** The color of the stroke to be applied to the shape.

**stroke.strokeDash** Defines the pattern of dashes and gaps used to paint the outline of the shape. It is
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

**commonAttributes.id** The id of the element.

**commonAttributes.class** The class of the element. Useful when combined with CSS.

**commonAttributes.x** Starting point of the text on the x-axis.

**commonAttributes.y** Starting point of the text on the y-axis.

**commonAttributes.fill** The color of the text.

**stroke.strokeLinecap** The ending shape of the line as `round`, `butt` or `square`.

**stroke.strokeWidth** The width of the stroke to be applied to the shape.

**stroke.strokeColor** The color of the stroke to be applied to the shape.

**stroke.strokeDash** Defines the pattern of dashes and gaps used to paint the outline of the shape. It is
defined as an array
of integers where each integer is the length of one dash.

# Custom Components

### Gauge

**min** Minimum value on the Gauge.

**max** Maximum value on the Gauge.

**value** Current value on the Gauge. Where the arrow is pointing to.

**type** The type of the gauge. Currently we have 'gradient' and 'five_values'.

**colors** If you want to override the default colors of the gauge. Can have 5 elements.

**commonAttributes.id** The id of the element.

**commonAttributes.class** The class of the element. Useful when combined with CSS.

**commonAttributes.x** Starting point of the text on the x-axis.

**commonAttributes.y** Starting point of the text on the y-axis.

# Parameters

You can set some parameters to have dynamic texts on your infographics. Depending on the param, the content will be
retrieved from a different system and substituted into the final SVG.

## Syntax

The params always have the next structure `#type%name%attributes#` where:

**type** Indicates where to obtain the value. Current allowed values are: DROOLS.

**name** The variable or value used to retrieve the value on the external source. Can be a variable name from Drools, a question of the form, ... 

**attributes** where to obtain the value. 

Some examples:

`#DROOLS%BMI%Value` the question value from the BMI question obtained from Drools.
`#DROOLS%BMI%Unit` the variable `Unit` from the BMI question obtained from Drools.
`#DROOLS%TheForm%Score` the variable `Score` at Form level.


# Example of use

An example of a template: a rectangle with a text that represent the score at form level. 

```
{
  "width": 960.0,
  "height": 300.0,
  "elementType": "SVG",
  "type": "freeLayout",
  "background": {
  },
  "elements": [
    {
      "elementType": "rectangle",
      "id": "rect",
      "ry": 18.0,
      "commonAttributes": {
        "fill": "#008080",
        "x": 170,
        "y": 75,
        "width": 790.0,
        "height": 150.0
      },
      "stroke": {
        "strokeWidth": 3.0,
        "strokeColor": "#000000",
        "strokeDash": [
          10,
          5,
          10,
          5
        ]
      }
    },
    {
      "elementType": "text",
      "contentText": "#DROOLS%TheForm%Score#",
      "fontSize": 45,
      "commonAttributes": {
        "fill": "#ffffff",
        "x": 280,
        "y": 105
      }
    }
  ]
}
```