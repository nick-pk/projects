# Build stage
FROM node:22.12.0 AS build

# Set the working directory
WORKDIR /app

# Copy package.json and package-lock.json
COPY ./package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the app code
COPY ./ ./

# Set build argument
ARG VITE_API_URL
ENV VITE_API_URL=${VITE_API_URL}

# Build the React app
RUN npm run build

# Production stage
FROM nginx:alpine

# Copy the built app from the build stage
COPY --from=build /app/dist /usr/share/nginx/html

# Expose port 80
EXPOSE 80

# Run Nginx to serve the React app
CMD ["nginx", "-g", "daemon off;"]