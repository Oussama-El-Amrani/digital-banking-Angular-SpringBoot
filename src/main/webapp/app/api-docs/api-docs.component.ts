import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import SwaggerUI from 'swagger-ui';

// Fix the type declarations for SwaggerUI specifically for this component
// This avoids conflicts with the installed @types/swagger-ui
const SwaggerUIBundle = SwaggerUI as any;

@Component({
  selector: 'app-api-docs',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="min-h-screen bg-white">
      <div class="container mx-auto px-4 py-8">
        <h1 class="text-3xl font-bold text-gray-900 mb-6">API Documentation</h1>
        <p class="text-gray-600 mb-6">
          Explore and test the Digital Banking API endpoints directly from this interactive documentation.
        </p>

        <div id="swagger-ui" class="mt-4"></div>
      </div>
    </div>
  `,
  styles: [`
    :host ::ng-deep .swagger-ui .topbar { display: none }
    :host ::ng-deep .swagger-ui .info { margin: 30px 0 }
    :host ::ng-deep .swagger-ui .info .title { font-size: 32px; font-weight: bold; color: #2c3e50; }
    :host ::ng-deep .swagger-ui .opblock-tag { font-size: 24px; margin: 30px 0 10px; }
    :host ::ng-deep .swagger-ui .opblock { margin-bottom: 15px; border-radius: 8px; }
    :host ::ng-deep .swagger-ui .opblock .opblock-summary { padding: 10px; }
    :host ::ng-deep .swagger-ui .btn { border-radius: 4px; }
    :host ::ng-deep .swagger-ui select { background-color: white; }
  `]
})
export class ApiDocsComponent implements OnInit {

  ngOnInit() {
    // Instead of importing JSON file directly, fetch it at runtime
    fetch('/api-docs.json')
      .then(response => response.json())
      .then(apiDocs => {
        // Initialize Swagger UI with the fetched JSON spec using the any-typed variable
        SwaggerUIBundle({
          dom_id: '#swagger-ui',
          spec: apiDocs,
          deepLinking: true,
          presets: [
            SwaggerUIBundle.presets.apis,
          ],
          plugins: [
            SwaggerUIBundle.plugins.DownloadUrl
          ],
          layout: "BaseLayout",
          docExpansion: 'list',
          tagsSorter: 'alpha',
          operationsSorter: 'alpha',
          defaultModelsExpandDepth: 1,
          defaultModelExpandDepth: 1,
          filter: true
        });
      })
      .catch(error => {
        console.error('Error loading API docs:', error);
        // Display error message in the UI
        const swaggerElement = document.getElementById('swagger-ui');
        if (swaggerElement) {
          swaggerElement.innerHTML = `<div class="p-4 bg-red-100 text-red-700 rounded">
            <h3 class="font-bold">Error loading API documentation</h3>
            <p>Could not load API docs. Please make sure the API server is running.</p>
          </div>`;
        }
      });
  }
}
