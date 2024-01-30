# Objective

This software execute templates in JSON format and covert them into SVG images.

# Standard Components

This is a list of the implemented components and the properties that are available for each of them. For some code
example, please check the test inside `infographic-engine-core` module.

### Template

This is the main component and any element must be inside this component. This will generate the SVG document with a
viewbox and a background.

**background.backgroundColor** The background color of the background.

**background.xRadius** As a rectangle, the radius of the corner.

**background.yRadius** As a rectangle, the radius of the corner.

**background.image** Use an image and not a color. Not compatible with the other `background` parameters.

**elements** Any other of the elements listed below.

Example:

```
{
  "background" : {
    "backgroundColor" : "#449911"
  },
  "commonAttributes" : {
    "widthUnit" : "PIXELS",
    "heightUnit" : "PIXELS",
    "x" : 128,
    "y" : 128,
  },
  "elementType" : "SVG",
  "elements" : [ ],
  "stroke" : {
  }
}
```

### Circle

**commonAttributes.id** The id of the element.

**commonAttributes.class** The class of the element. Useful when combined with CSS.

**commonAttributes.x** The position of the center of the circle.

**commonAttributes.y** The position of the center of the circle.

**commonAttributes.fill** The color of the circle.

**commonAttributes.style** Svg Style definition as string that will be injected on the component.

**commonAttributes.gradient** You can define a gradient rather than a color for filling the element.

**stroke.strokeLinecap** The ending shape of the line as `round`, `butt` or `square`.

**stroke.strokeWidth** The width of the stroke to be applied to the shape.

**stroke.strokeColor** The color of the stroke to be applied to the shape.

**stroke.strokeDash** Defines the pattern of dashes and gaps used to paint the outline of the shape. It is
defined as an array of integers where each integer is the length of one dash.

Example:

```
{
  ...
  "elements" : [ {
    "commonAttributes" : {
      "widthUnit" : "PIXELS",
      "heightUnit" : "PIXELS",
      "x" : 128,
      "y" : 128
    },
    "elementType" : "CIRCLE",
    "id" : "circle0",
    "radius" : 128,
    "stroke" : {
      "strokeWidth" : 0.0,
      "strokeColor" : "black",
      "strokeLinecap" : "BUTT"
    }
  } ]
}
```

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

**commonAttributes.gradient** You can define a gradient rather than a color for filling the element.

**stroke.strokeLinecap** The ending shape of the line as `round`, `butt` or `square`.

**stroke.strokeWidth** The width of the stroke to be applied to the shape.

**stroke.strokeColor** The color of the stroke to be applied to the shape.

**stroke.strokeDash** Defines the pattern of dashes and gaps used to paint the outline of the shape. It is
defined as an array of integers where each integer is the length of one dash.

Example:

```
{
  ...
  "elements" : [ {
    "commonAttributes" : {
      "width" : 64,
      "widthUnit" : "PIXELS",
      "height" : 128,
      "heightUnit" : "PIXELS",
      "x" : 128,
      "y" : 128,
      "style" : "",
      "fill" : "#ff00ff"
    },
    "elementType" : "ELLIPSE",
    "id" : "ellipse0",
    "stroke" : {
      "strokeWidth" : 1.0,
      "strokeColor" : "black"
    }
  } ]
}
```

### Image

An image. Can be an image to embed or dynamic a path to a resource. The resource can be on the resources folder of the
application, or on a folder defined by the System ENV variable `FILES_FOLDER`.

**content** The image as base64 that will be encrusted on the template.

**href** A link if needed.

**resource** A resource path where the image will be obtained after the rule's execution. Can exist on the resource
folder or in a folder defined by the System ENV variable `FILES_FOLDER`.

**resourceAlreadyInBase64** if the resource is already codified as base64 image or not.

**commonAttributes.id** The id of the element.

**commonAttributes.class** The class of the element. Useful when combined with CSS.

**commonAttributes.x** Starting point of the image on the x-axis in pixels.

**commonAttributes.y** Starting point of the image on the y-axis in pixels.

Example:

```
{
  ...
  "elements" : [ {
    "commonAttributes" : {
      "width" : 282,
      "widthUnit" : "PIXELS",
      "height" : 254,
      "heightUnit" : "PIXELS",
      "x" : 1272,
      "y" : 67,
      "style" : ""
    },
    "content" : "iVBORw0KGgoAAAANSUhEUgAAA...",
    "elementType" : "IMAGE",
    "id" : "image0"
  } ]
}
```

### Line

**x2Coordinate** Ending point of the line on the x-axis in pixels.

**y2Coordinate** Ending point of the line on the y-axis in pixels.

**commonAttributes.x** Starting point of the line on the x-axis in pixels.

**commonAttributes.y** Starting point of the line on the y-axis in pixels.

**commonAttributes.fill** The color of the line.

**commonAttributes.style** Svg Style definition as string that will be injected on the component.

**commonAttributes.gradient** You can define a gradient rather than a color for filling the element.

**stroke.strokeLinecap** The ending shape of the line as `round`, `butt` or `square`.

**stroke.strokeWidth** The width of the stroke to be applied to the shape.

**stroke.strokeColor** The color of the stroke to be applied to the shape.

**stroke.strokeDash** Defines the pattern of dashes and gaps used to paint the outline of the shape. It is
defined as an array of integers where each integer is the length of one dash.

Example:

```
{
  ...
  "elements" : [ {
    "commonAttributes" : {
      "widthUnit" : "PIXELS",
      "heightUnit" : "PIXELS",
      "x" : 50,
      "y" : 50,
      "style" : ""
    },
    "elementType" : "LINE",
    "id" : "line0",
    "stroke" : {
      "strokeWidth" : 1.0,
      "strokeColor" : "black",
      "strokeLinecap" : "BUTT"
    },
    "x2" : 256,
    "y2" : 256
  } ]
}
```

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

**commonAttributes.gradient** You can define a gradient rather than a color for filling the element.

**commonAttributes.verticalAlign**

**stroke.strokeLinecap** The ending shape of the line as `round`, `butt` or `square`.

**stroke.strokeWidth** The width of the stroke to be applied to the shape.

**stroke.strokeColor** The color of the stroke to be applied to the shape.

**stroke.strokeDash** Defines the pattern of dashes and gaps used to paint the outline of the shape. It is
defined as an array of integers where each integer is the length of one dash.

Example:

```
{
  ...
  "elements" : [ {
    "commonAttributes" : {
      "width" : 128,
      "widthUnit" : "PIXELS",
      "height" : 128,
      "heightUnit" : "PIXELS",
      "x" : 128,
      "y" : 128,
      "style" : "",
      "gradient" : {
        "elementType" : "GRADIENT",
        "stops" : [ {
          "color" : "#ff0000",
          "elementType" : "GRADIENT_STOP",
          "offset" : 0.0,
          "opacity" : 1.0
        }, {
          "color" : "#0000ff",
          "elementType" : "GRADIENT_STOP",
          "offset" : 1.0,
          "opacity" : 1.0
        } ]
      }
    },
    "elementType" : "RECTANGLE",
    "id" : "rectangle0",
    "stroke" : {
      "strokeWidth" : 0.0,
      "strokeColor" : "black",
      "strokeLinecap" : "BUTT"
    },
    "xRadius" : 0,
    "xradius" : 0,
    "yRadius" : 0,
    "yradius" : 0
  } ]
}
```

### Script

Used in combination with other components, allows to include some basic javascript in the image to make it interactive.

**script** The javascript that will be available on the image.

Example:

```
{
  ...
  "elements" : [ {
    "commonAttributes" : {
      "widthUnit" : "PIXELS",
      "heightUnit" : "PIXELS",
      "x" : 128,
      "y" : 128,
      "style" : ""
    },
    "elementType" : "CIRCLE",
    "id" : "circle0",
    "radius" : 128,
    "stroke" : {
      "strokeWidth" : 0.0,
      "strokeColor" : "black",
      "strokeLinecap" : "BUTT"
    }
  }, {
    "commonAttributes" : {
      "widthUnit" : "PIXELS",
      "heightUnit" : "PIXELS",
      "x" : 0,
      "y" : 0,
      "style" : ""
    },
    "elementType" : "SCRIPT",
    "id" : "script1",
    "script" : "function getColor() {\n      const R = Math.round(Math.random() * 255)\n        .toString(16)\n        .padStart(2, \"0\");\n\n      const G = Math.round(Math.random() * 255)\n        .toString(16)\n        .padStart(2, \"0\");\n\n      const B = Math.round(Math.random() * 255)\n        .toString(16)\n        .padStart(2, \"0\");\n\n      return `#${R}${G}${B}`;\n    }\n\n    document.querySelector(\"circle\").addEventListener(\"click\", (e) => {\n        e.target.style.fill = getColor();\n    });\n",
    "stroke" : {
      "strokeWidth" : 0.0,
      "strokeColor" : "black",
      "strokeLinecap" : "BUTT"
    }
  } ]
}
```

### Text

Sets a text in the image. If you want to create a new line separator, use the `\n` character. The system will
automatically split the line if founds this character.

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
several lines if needed. Cannot be used with maxLineWidth parameter.

**maxLineWidth** Maximum length of a line in pixels. Do not split complete words and distribute the text on
several lines if needed. The font must be installed on the backend. Cannot be used with maxLineLength parameter.

**maxParagraphHeight** Maximum height of the paragraph of the text. If the text does not fit, the fontSize will be
decreased until it fits.

**textAlign** Alignment of a text in a paragraph. Must be combined with `maxLineLength` or `maxLineWidth`. Allowed
values are `left`, `right`, `center`, `justify`. For 'justify' option, the font must be installed on the backend.

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
defined as an array of integers where each integer is the length of one dash.

Example:

```
{
  ...
  "elements" : [ {
    "commonAttributes" : {
      "widthUnit" : "PIXELS",
      "heightUnit" : "PIXELS",
      "x" : 0,
      "y" : 0,
      "style" : ""
    },
    "contentText" : "Lorem ipsum dolor\n...",
    "dxUnit" : "PIXELS",
    "dyUnit" : "PIXELS",
    "elementType" : "TEXT",
    "fontFamily" : "Monday Donuts",
    "fontSize" : 8,
    "fontVariant" : "NORMAL",
    "id" : "text0",
    "maxLineWidth" : 200,
    "maxParagraphHeight" : 90,
    "rotate" : 0,
    "stroke" : {
      "strokeWidth" : 0.0,
      "strokeColor" : "black",
      "strokeLinecap" : "BUTT"
    },
    "textAlign" : "JUSTIFY",
    "textLengthUnit" : "PIXELS"
  } ]
}
```

### PATH

**points** An array of point. Each point is a (x, y) value. If **commonAttributes.x** and/or **commonAttributes.y** are
set, will be used as the starting point. Define the point as absolute coordinates.

**commonAttributes.id** The id of the element.

**commonAttributes.class** The class of the element. Useful when combined with CSS.

**commonAttributes.x** Starting point of the text on the x-axis. (Optional)

**commonAttributes.y** Starting point of the text on the y-axis. (Optional)

**commonAttributes.fill** If set, will fill all the area defined by the path.

**stroke.strokeLinecap** The ending shape of the line as `round`, `butt` or `square`.

**stroke.strokeWidth** The width of the stroke to be applied to the shape.

**stroke.strokeColor** The color of the stroke to be applied to the shape.

**stroke.strokeDash** Defines the pattern of dashes and gaps used to paint the outline of the shape. It is
defined as an array of integers where each integer is the length of one dash.

Example:

```
{
  ...
  "elements" : [ {
    "commonAttributes" : {
      "widthUnit" : "PIXELS",
      "heightUnit" : "PIXELS",
      "x" : 0,
      "y" : 0,
      "style" : "",
      "fill" : "none"
    },
    "elementType" : "PATH",
    "id" : "path0",
    "points" : [ {
      "x" : 50,
      "y" : 50
    }, {
      "x" : 100,
      "y" : 0
    }, {
      "x" : 200,
      "y" : 150
    } ],
    "stroke" : {
      "strokeWidth" : 1.0,
      "strokeColor" : "black",
      "strokeDash" : [ 5, 5, 10, 10, 1 ],
      "strokeLinecap" : "ROUND"
    }
  } ]
}
```

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

Example:

```
{
  ...
  "elements" : [ {
    "colors" : [ "#ff0000", "#ff8000", "#ffd900", "#87aa00", "#678100" ],
    "commonAttributes" : {
      "widthUnit" : "PIXELS",
      "heightUnit" : "PIXELS",
      "x" : 0,
      "y" : 0,
      "style" : ""
    },
    "elementType" : "GAUGE",
    "flip" : true,
    "id" : "gauge0",
    "max" : 5.0,
    "min" : 1.0,
    "stroke" : {
      "strokeWidth" : 0.0,
      "strokeColor" : "black",
      "strokeLinecap" : "BUTT"
    },
    "type" : "FIVE_VALUES",
    "value" : 4.0
  } ]
}
```

# Parameters

You can set some parameters to have dynamic texts on your infographics. Depending on the param, the content will be
retrieved from a different system and substituted into the final SVG.

## Syntax

The params always have the next structure `#type%name%attributes#` where:

**type** Indicates where to obtain the value. Current allowed values are: DROOLS.

**name** The variable or value used to retrieve the value on the external source. Can be a variable name from Drools, a
question of the form, ...

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

# HowTo create an infographic

Steps of how to generate a form and shown an infographic from it.

1. Create a form in Webform.
2. Create the rules on ABCD.
3. Link the ABCD form on Webforms.
4. Export ABCD rules and deploy them into the Base Form Drools docker container.
5. Export Webforms Metadata and deploy them into the Base Form Drools docker container.
6. Generate a Form Result from Webforms. Using XFormsRunner or any other application where you can submit forms.
7. Send the FormResult
   to: `https://testing.biit-solutions.com/base-form-drools-engine-backend/drools/element/forms/results` and get the
   Drools Results `DroolsSubmittedForm`. Store it as a JSON. Beware that the endpoint return an array and you only need
   one item.
8. Generate a `SVGTemplate` on a test with the desired structure. Populate all fields with the ABCD variables.
9. Execute the Drools Results from step `7` inside the created test. Use the `droolsResultController` bean for this purpose.
10. When debugging the test, get the `SvgTemplate` and convert it to JSON with its already existing method `.toJson()`.
11. Put the JSON into the infographic docker. Remember to create a folder with the same name of the ABCD form, and
    an `index.json` file that points to the JSON generated.
12. Use the `DroolsSubmittedForm` for testing the infographic docker. Access
    to `https://testing.biit-solutions.com/infographic-engine-backend/svg/create/drools/plain` and use the file as
    the input. 



