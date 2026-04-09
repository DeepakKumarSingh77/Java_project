# Build GoogleDocs

## Project Overview
This is a simple Java proof-of-concept for a document editor system similar to a minimal Google Docs design.
The project demonstrates:
- document structure with elements such as text and image
- rendering document elements
- saving documents using a persistence abstraction
- clean object-oriented design using SOLID principles

## Requirements
1. Create a `Document` that can contain multiple `DocumentElement` items.
2. Support at least two element types: `TextElement` and `ImageElement`.
3. Allow document elements to be rendered.
4. Provide a persistence layer to save documents, with at least one concrete implementation (`SQL_DB`).
5. Keep the code easy to extend without modifying existing element or persistence code.
6. Follow clean design principles, especially:
   - Single Responsibility Principle
   - Open/Closed Principle
   - Dependency Inversion Principle

## What was implemented
- `DocumentElement` is an abstract base class for all content elements.
- `TextElement` and `ImageElement` extend `DocumentElement` and implement their own `render()` behavior.
- `Document` manages a list of `DocumentElement` objects and renders them in sequence.
- `Persistance_DB` is an abstraction for saving a document.
- `SQL_DB` and `MongoDB` are concrete persistence implementations.
- `DocumentEditor` uses dependency injection to work with any `Persistance_DB` implementation.
- `Document_Render` is separated from editing and saving responsibilities.

## Design Principles Followed
- **Single Responsibility Principle:**
  - `Document` only manages elements and rendering.
  - `DocumentEditor` only adds content and saves documents.
  - `Document_Render` only handles document rendering.
  - Persistence classes only handle saving documents.
- **Open/Closed Principle:**
  - New element types can be added by extending `DocumentElement`.
  - New persistence mechanisms can be added by extending `Persistance_DB`.
  - Existing classes do not need modification when extending functionality.
- **Dependency Inversion Principle:**
  - `DocumentEditor` depends on the abstraction `Persistance_DB`, not concrete persistence classes.
  - This allows swapping `SQL_DB` for `MongoDB` or any other persistence provider.

## How to run
1. Compile:
   ```bash
   javac GoogleDocs.java
   ```
2. Run:
   ```bash
   java GoogleDocs
   ```

## Notes for GitHub readers
This repository is intentionally small and focused on demonstrating clean design in a document editor context.
The code is structured so future features (new document elements, export formats, or storage backends) can be added with minimal change to existing classes.
