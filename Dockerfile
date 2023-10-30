FROM ghcr.io/vmware-tanzu-labs/educates-base-environment:2.6.16

# All the direct Downloads need to run as root as they are going to /usr/local/bin
USER root

RUN code-server --install-extension redhat.java@1.24.2023101204
RUN code-server --install-extension redhat.vscode-yaml@1.14.0
RUN code-server --install-extension vscjava.vscode-java-debug@0.54.0
RUN code-server --install-extension vscjava.vscode-maven@0.42.0
RUN code-server --install-extension vscjava.vscode-java-dependency@0.23.1
RUN code-server --install-extension vscjava.vscode-java-test@0.39.1
RUN code-server --install-extension vmware.vscode-spring-boot@1.49.0
 
RUN yum install moreutils maven moreutils unzip zip gcc gcc-c++ make zlib-devel -y

RUN curl -s "https://get.sdkman.io" | bash && \
    echo "sdkman_auto_answer=true" > $HOME/.sdkman/etc/config && \
    echo "sdkman_auto_selfupdate=false" >> $HOME/.sdkman/etc/config
# Don't forget to update the `sdk use` commands in the workshop instructions if you update the versions    
RUN bash -c "source $HOME/.sdkman/bin/sdkman-init.sh && sdk install java 22.3.1.r17-grl && export GRAALVM_HOME=/home/eduk8s/.sdkman/candidates/java/22.3.1.r17-grl/ && gu install native-image && sdk install java 17.0.7-librca"
RUN echo "export GRAALVM_HOME=/home/eduk8s/.sdkman/candidates/java/22.3.1.r17-grl/" >> $HOME/.bashrc

# TBS
RUN curl -L -o /usr/local/bin/kp https://github.com/buildpacks-community/kpack-cli/releases/download/v0.12.0/kp-linux-amd64-0.12.0 && \
  chmod 755 /usr/local/bin/kp

# Knative
RUN curl -L -o /usr/local/bin/kn https://github.com/knative/client/releases/download/knative-v1.12.0/kn-linux-amd64 && \
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