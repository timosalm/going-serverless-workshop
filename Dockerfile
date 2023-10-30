FROM registry.tanzu.vmware.com/tanzu-application-platform/tap-packages@sha256:c184e9399d2385807833be0a9f1718c40caa142b6e1c3ddf64fa969716dcd4e3

# All the direct Downloads need to run as root as they are going to /usr/local/bin
USER root

RUN apt-get update && apt-get install -y maven moreutils unzip zip build-essential libz-dev

RUN curl -s "https://get.sdkman.io" | bash && \
    echo "sdkman_auto_answer=true" > $HOME/.sdkman/etc/config && \
    echo "sdkman_auto_selfupdate=false" >> $HOME/.sdkman/etc/config
# Don't forget to update the `sdk use` commands in the workshop instructions if you update the versions    
RUN bash -c "source $HOME/.sdkman/bin/sdkman-init.sh && sdk install java 22.3.1.r17-grl && export GRAALVM_HOME=/home/eduk8s/.sdkman/candidates/java/22.3.1.r17-grl/ && gu install native-image && sdk install java 17.0.7-librca"
RUN echo "export GRAALVM_HOME=/home/eduk8s/.sdkman/candidates/java/22.3.1.r17-grl/" >> $HOME/.bashrc

# TBS
RUN curl -L -o /usr/local/bin/kp https://github.com/vmware-tanzu/kpack-cli/releases/download/v0.10.0/kp-linux-amd64-0.10.0 && \
  chmod 755 /usr/local/bin/kp

# Knative
RUN curl -L -o /usr/local/bin/kn https://github.com/knative/client/releases/download/knative-v1.10.0/kn-linux-amd64 && \
    chmod 755 /usr/local/bin/kn

# hey 
RUN curl -L -o /usr/local/bin/hey https://hey-release.s3.us-east-2.amazonaws.com/hey_linux_amd64 && \
    chmod 755 /usr/local/bin/hey

# dive 
RUN curl -LO https://github.com/wagoodman/dive/releases/download/v0.10.0/dive_0.10.0_linux_amd64.tar.gz \
    && tar xvzf dive_0.10.0_linux_amd64.tar.gz -C /usr/local/bin dive \
    && chmod 755 /usr/local/bin/dive && rm dive_0.10.0_linux_amd64.tar.gz

USER 1001

RUN fix-permissions /home/eduk8s