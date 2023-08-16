# TODO-LIST APPLICATION: Neat
 An application that helps you organize and keep track of various tasks that you need to do. Built in Android Studio Framework using XML language for front-end and Java language for back-end.


# USER GUIDE ---
This app is meant to help users organize their tasks into categories, and mark them when done. Tasks can be added to multiple categories if needed. When marked complete, tasks aren’t deleted, but can be filtered out and not shown.

Categories cannot have the same name, and must be a string of alphanumeric characters less than 15 characters long. The same goes for each task. There is no limit to how many tasks and categories are added.


# CATEGORIES ---

Adding New Category: <br/>

When on a screen showing a list of categories, click on the FloatingActionButton in the lower right. A popup will appear, allowing you to change its name and image. Click ‘Add’ to add it to the app.

Editing Category : <br/>

When on a screen showing a list of categories, long hold on one of the categories to edit it. A popup will appear, allowing you to change its name and image. Click ‘Save’ to add it to the app.

Delete Category : <br/>

When on a screen showing a list of categories, long hold on one of the categories to edit it. A popup will appear, with an option to delete the task. Note that there is no confirmation for this. Tasks will automatically be removed from this category.

Changing Category Icon :

When on a popup to add/edit a category, click on the image. A popup will appear, allowing you to change its design. There are nice icons to choose from, and ten colors. A button can be toggled, allowing you to change the color of the foreground (icon) or background. Remember to click ‘Save’ to properly update the app.


# TASKS ---

Adding New Task : <br/>

When on a screen showing a list of tasks, click on the FloatingActionButton in the lower right. A popup will appear, allowing you to change its name, date and categories that it’s in. To select multiple categories, open the drop down menu and check each one. Click ‘Add’ to add it to the app. Note that tasks are automatically added to the “ALL TASKS” category.

Editing Task :<br/>

When on a screen showing a list of tasks, click on the task itself (not the checkbox). A popup will appear, allowing you to change its name, date and categories that it’s in. To select multiple categories, open the drop down menu and check each one. Click ‘Save’ to update the task. Note that tasks cannot be removed from the “ALL TASKS” category.

As a side note, you can mark a task as “complete” by making sure the checkmark next to it is checked

Deleting Task :<br/>

When on a screen showing a list of tasks, click on the task itself (not the checkbox) to edit it. A popup will appear, with an option to delete the task. Note that there is no confirmation for this. This task will automatically be removed from all categories.

Filtering Tasks :<br/>

Clicking on the menu in the top left opens up a drawer with navigation options. One of these options is called ‘All Tasks’, ‘Completed Tasks’ or ‘Incomplete Tasks.’ Clicking this will change the visibility of which tasks will be shown in each category. Note that this will not affect the amount of tasks in each category.

A task is considered complete if the check box next to it is marked, and incomplete otherwise. The ‘All Tasks’ option isn’t filtered.



