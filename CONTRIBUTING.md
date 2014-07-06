How to Contribute
==============
PersistenceLib takes pride in being open source and community driven.  However, there are guidelines that must be adhered to when making pull requests to accelerate productivity.

Code Style
--------------
When contributing changes, certain requirements should be adhered to standardize the library.  PersistenceLib uses the [Google Java Code Style](http://google-styleguide.googlecode.com/svn/trunk/javaguide.html) for controlling the style of the project.  A code style profile for your preferred IDE can be downloaded from [here](https://code.google.com/p/google-styleguide/source/browse/trunk/).

Some exceptions to the Google Java Code Style include:
* With the exception of enum constants, code should not be wrapped.
* Each time a new block or block-like construct is opened, the indent increases by four spaces. When the block ends, the indent returns to the previous indent level. The indent level applies to both code and comments throughout the block.
* One blank paragraph line-that is a line containing only the aligned leading asterisks and a paragraph tag (* <p>)-appears between paragraphs.
* No paragraph tag should be included before the group of "at-clauses" if present.
* Descriptions following an "at-clause" should begin on the following line and should all be indented.

Commits
--------------
Make your commits as small as possible.  Keeping commits small allows you to write more concise commit descriptions as well making changes easier to track.

Commit Descriptions
--------------
Always write a comment when committing something to the repository. Your comment should be brief and to the point, describing what was changed and possibly why. If you made several changes, write one line or sentence about each part. If you find yourself writing a very long list of changes, consider splitting your commit into smaller parts, as described earlier. Prefixing your comments with identifiers like Fix or Add is a good way of indicating what type of change you did. It also makes it easier to filter the content later, either visually, by a human reader, or automatically, by a program.

If you fixed a specific bug or implemented a specific change request, I also recommend to reference the bug or issue number in the commit message.

Here are some examples of good commit messages:

	Changed paragraph separation from indentation to vertical space.
		Fix: Extra image removed.
		Fix: CSS patched to give better results when embedded in javadoc.
		Add: A javadoc {@link} tag in the lyx, just to show it's possible.
		...
		Fix: Fixed bug #1938.
		Add: Implemented change request #39381.
    