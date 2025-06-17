FROM node:18 AS build

WORKDIR /app

COPY . .
RUN npm install
RUN npm run build -- --configuration production


FROM nginx:alpine

COPY nginx.conf /etc/nginx/conf.d/default.conf

COPY --from=build /app/dist/delight-nook-angular/browser /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]