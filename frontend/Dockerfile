FROM node:alpine AS build

# Setup the working directory
WORKDIR /usr/src/app

# Copy package.json and package-lock.json
COPY package.json package-lock.json ./

# Install dependencies
RUN npm install

# Copy other files and folders to the working directory
COPY . .

# Build Angular application in PROD mode
RUN npm run build --prod

FROM nginx:alpine

COPY --from=build /usr/src/app/dist/frontend-app/browser /usr/share/nginx/html

EXPOSE 80
