# Deadline

Modify this file to satisfy a submission requirement related to the project
deadline. Please keep this file organized using Markdown. If you click on
this file in your GitHub repository website, then you will see that the
Markdown is transformed into nice looking HTML.

## Part 1: App Description

> Please provide a firendly description of your app, including the
> the primary functions available to users of the app. Be sure to
> describe exactly what APIs you are using and how they are connected
> in a meaningful way.

> **Also, include the GitHub `https` URL to your repository.**

GitHub repository URL: https://github.com/AdityaDeb12779/cs1302-api

My app allows a user to search up whatever dish or food they want
to create. Essentially, a user can search up a dish, and my app provides
a recipe for either the searched dish , or a dish related to what the
user searched up. My app provides the nutrition labels of a dish
(i.e. gluten-free, vegetarian, soy-free, etc.), the picture of the dish,
the servings of the dish, the ingredients of the dish,
the nutrition facts of the dish (i.e. total carbs, total sugars, total fat, etc.),
and a link to the website with the dish instructions.

This app uses 2 APIs, the first one being the Edamam Recipe Search API.
With this API, you can query a recipe search term, and it will provide
a JSon string with an object of all recipe results. I connect this API
with the Edamam Nutrition Analysis API (Dr. Cotterell said using these
APIs were fine). With the Edamam Nutrition Analysis API, you can query
an ingredient with its quantity and it will return a JSon string containing
the nutritional components (i.e. carbs, sugars, fats, protein, etc.) of that
ingredient. The Recipe Search API has a variable which is a String
array that contains the lines of ingredients in the recipe. Using each
ingredient in that variable, I can query the Nutrition Analysis API
and total up the nutritional values of all the ingredients, to obtain
the and display a nutrition label of the recipe.


## Part 2: New

> What is something new and/or exciting that you learned from working
> on this project?

Some things new I learned from this project were about the cool
GUI elements and functions that JavaFX provides. Throughout this
project, I was surfing through the JavaFX Javadocs to find nodes
and methods that were able to do things I was looking for. For
example, I learned about the StackPane node, and it was useful being
able to overlay nodes in cool manners. Additionally, I learned about
how to style other components, learn a bit of CSS code to set the
style of some components, and learn how to allow the user to copy
text from the scene graph.

## Part 3: Retrospect

> If you could start the project over from scratch, what do
> you think might do differently and why?

Somethings I would do differently if I had more time would be
to definitely invest some time in making the GUI look nice and
creating an appealing GUI. In my opinion, the one I have right
now is nicely formatted, however it is not visually appealing.
