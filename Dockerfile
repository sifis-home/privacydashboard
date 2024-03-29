# Stage that builds the application, a prerequisite for the running stage
FROM maven:3.8.6-eclipse-temurin-18 as build
RUN curl -fsSL https://deb.nodesource.com/setup_18.x | bash -
RUN apt-get update -qq && apt-get install -qq --no-install-recommends nodejs

# Stop running as root at this point
RUN useradd -m myuser
WORKDIR /usr/src/app/
RUN chown myuser:myuser /usr/src/app/
USER myuser

# Copy pom.xml and prefetch dependencies so a repeated build can continue from the next step with existing dependencies
COPY --chown=myuser pom.xml ./
RUN mvn dependency:go-offline -Pproduction

# Copy all needed project files to a folder
COPY --chown=myuser:myuser src src
COPY --chown=myuser:myuser frontend frontend
COPY --chown=myuser:myuser package.json ./

# Using * after the files that are autogenerated so that so build won't fail if they are not yet created
COPY --chown=myuser:myuser package-lock.json* pnpm-lock.yaml* webpack.config.js* ./


# Build the production package, assuming that we validated the version before so no need for running tests again
RUN mvn clean package -DskipTests -Pproduction

# Running stage: the part that is used for running the application
FROM openjdk:22-oraclelinux8
COPY --from=build /usr/src/app/target/*.jar /usr/app/app.jar
RUN useradd -m myuser
USER myuser
EXPOSE 11002
CMD java -jar /usr/app/app.jar
