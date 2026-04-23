# ============================================================
# Dockerfile — Frontend (Nginx)
# Serves static HTML/CSS/JS files via Nginx web server
# ============================================================

# Use the official lightweight Nginx image (Alpine-based)
FROM nginx:1.25-alpine

# Remove the default nginx welcome page
RUN rm -rf /usr/share/nginx/html/*

# Copy your frontend files into Nginx's web root
COPY . /usr/share/nginx/html

# Copy custom nginx config (handles API proxy + routing)
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Expose port 80 (HTTP)
EXPOSE 80

# Nginx starts automatically — no CMD needed (inherited from base image)