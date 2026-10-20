FROM node:22-alpine AS dependencies

WORKDIR /app
COPY package.json package-lock.json .npmrc ./
RUN npm ci

FROM node:22-alpine AS build

WORKDIR /app
COPY --from=dependencies /app/node_modules ./node_modules
COPY . .
RUN mkdir -p public
ARG NEXT_PUBLIC_API_BASE_URL=http://localhost:8080
ENV NEXT_PUBLIC_API_BASE_URL=${NEXT_PUBLIC_API_BASE_URL}
RUN npm run build

FROM node:22-alpine AS runtime

WORKDIR /app
ENV NODE_ENV=production
ENV PORT=3000
RUN addgroup -S app && adduser -S -G app app
COPY --from=build --chown=app:app /app/.next/standalone ./
COPY --from=build --chown=app:app /app/.next/static ./.next/static
COPY --from=build --chown=app:app /app/public ./public

USER app
EXPOSE 3000
CMD ["node", "server.js"]
