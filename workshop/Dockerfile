FROM registry.tanzu.vmware.com/tanzu-application-platform/tap-packages@sha256:a8870aa60b45495d298df5b65c69b3d7972608da4367bd6e69d6e392ac969dd4

# All the direct Downloads need to run as root as they are going to /usr/local/bin
USER root

RUN apt-get update && apt-get install -y maven moreutils unzip zip

RUN curl -s "https://get.sdkman.io" | bash && \
    echo "sdkman_auto_answer=true" > $HOME/.sdkman/etc/config && \
    echo "sdkman_auto_selfupdate=false" >> $HOME/.sdkman/etc/config
RUN bash -c "source $HOME/.sdkman/bin/sdkman-init.sh && sdk install java 22.1.0.r17-grl && sdk install java 11.0.15-librca"

# TBS
RUN curl -L -o /usr/local/bin/kp https://github.com/vmware-tanzu/kpack-cli/releases/download/v0.4.2/kp-linux-0.4.2 && \
  chmod 755 /usr/local/bin/kp

# Knative
RUN curl -L -o /usr/local/bin/kn https://github.com/knative/client/releases/download/knative-v1.1.0/kn-linux-amd64 && \
    chmod 755 /usr/local/bin/kn

# hey 
RUN curl -L -o /usr/local/bin/hey https://hey-release.s3.us-east-2.amazonaws.com/hey_linux_amd64 && \
    chmod 755 /usr/local/bin/hey

# dive 
RUN curl -LO https://github.com/wagoodman/dive/releases/download/v0.10.0/dive_0.10.0_linux_amd64.tar.gz \
    && tar xvzf dive_0.10.0_linux_amd64.tar.gz -C /usr/local/bin dive \
    && chmod 755 /usr/local/bin/dive

#RUN apt-get update && apt-get install -y unzip openjdk-11-jdk moreutils

RUN rm dive_0.10.0_linux_amd64.tar.gz

USER 1001
COPY --chown=1001:0 . /home/eduk8s/
RUN fix-permissions /home/eduk8s