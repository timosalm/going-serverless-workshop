Serverless doesn’t mean that there are no servers, it means that you don’t have to care about them.

Serverless techniques and technologies can be group into two areas.
The first one is Backend as a Service with the goal of replacing server-side, self-managed components with off-the-shelf services. 
This frees you from writing entire logical components and requires you to integrate your applications with the specific interface a vendor provides. Examples for Backend as a Service offerings are Cloud-accessible databases like Parse, Firebase and authentication services like Okta or Auth0.

The second group is Functions as a Service. 
That’s a new way of building and deploying server-side software, oriented around deploying individual functions.
FaaS drives a very different type of application architecture through a fundamentally event-driven model, a much more granular form of deployment, and the need to persist state outside of your FaaS components.
By using these ideas, and related ones like single-page applications, such architectures combined with BaaS remove much of the need for a traditional always-on server component.
One of the most popular implementations of a Functions-as-a-Service platform at present is AWS lambda.

The key is that with both, BaaS and FaaS, you don't have to manage your own server hosts or server processes and can focus on business value!

##### Key characteristics
A Serverless service …
- does not require managing a long-lived host or application instance
- self auto-scales and auto-provisions, dependent on load
- has implicit high availability
- has performance capabilities defined in terms other than host size/count
- has costs that are based on precise usage, up from and down to zero usage
