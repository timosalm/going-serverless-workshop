**Serverless doesn’t mean that there are no servers, it means that you don’t have to care about them.**

Serverless techniques and technologies can be **group into two areas**:
- The first one is **Backend as a Service** with the goal of **replacing server-side, self-managed components with off-the-shelf services**. This frees you from writing entire logical components and requires you to integrate your applications with the specific interface a vendor provides. Examples for Backend as a Service offerings are Cloud-accessible databases like Parse, Firebase and authentication services like Okta or Auth0.
- The second group is **Functions as a Service**.  That’s a **new way of building and deploying server-side software, oriented around deploying individual functions**. FaaS drives a very different type of application architecture through a fundamentally event-driven model, a much more granular form of deployment, and the need to persist state outside of your FaaS components. By using these ideas, and related ones like single-page applications, such architectures combined with BaaS **remove much of the need for a traditional always-on server component**. One of the most popular implementations of a Functions-as-a-Service platform at present is AWS lambda.

The key is that **with both**, BaaS and FaaS, **you don't have to manage your own server hosts or server processes and can focus on business value**!

##### Key characteristics
A Serverless service …
- does not require managing a long-lived host or application instance
- self auto-scales and auto-provisions, dependent on load
- has implicit high availability
- has performance capabilities defined in terms other than host size/count
- has costs that are based on precise usage, up from and down to zero usage

##### Why Serverless?
The biggest advantage of Serverless is that it **removes so much of the complexity of building, deploying, and operating applications** in production, and at scale. 
With FaaS for example, packaging and deploying a function is very simple compared to deploying your application on Kubernetes or even an entire server. All you have to do is to package all your code into an archive and uploading it. 

Another benefit of Serverless is that **scaling happens automatically**, with no effort. 
With this capability Serverless **reduces resource cost** because you don’t have to over-provision and pay for the capacity necessary to handle your peak expected load, even when for example your application isn’t experiencing that load. 
So for any application that has inconsistent load, you will see resource cost savings by using Serverless. 

With BaaS you have less logic to develop yourself and less operations work and with FaaS software development and the deployment is simplified because much of the infrastructural code is moved out to the platform and you just have to upload basic code units which leads to **reduced labor cost**.

Finally, Serverless also **reduces risk** since the number of different technologies you are responsible for directly operating is significantly reduced and the expected downtime of components is reduced.

 
##### Drawbacks / Limitations of Serverless
Until now Serverless really sounds great but there are also drawbacks and limitations.

One of them is that the cost model is based on precise usage and **with the rapid autoscaling** comes the **risk of unpredictable costs** because when big demand comes, it will be served, and you have to pay for it.
Of course for most the offerings you can set boundaries for the scaling behavior and you can also perform load testing to predict cost.

Another one is that most **Serverless applications are stateless** and by definition they **have to interact with other, stateful components** to persist information which **introduces latency, as well as some complexity**. 
Additionally, much of the **inter-component communication happens via HTTP APIs, which can be slower** than other transports and the more components communicating, the more latency will be inherent in a Serverless application. 

Serverless platforms can scale out to a lot of container instances very quickly. This **can cause trouble for downstream systems that cannot increase capacity as quickly** or that have trouble serving a highly concurrent workload, such as traditional relational databases and external APIs with enforced rate limits.

An obvious limitation of Serverless is a **loss of absolute control** over configuration, security, and for example the performance of application code and performance of the underlying Serverless platform.

**Debugging, and local testing is also more difficult** and a **Vendor lock-in** seems like another inherent limitation of Serverless applications. However, different Serverless platform vendors enforce different levels of lock-in, through for example their choice of integration patterns and APIs.

