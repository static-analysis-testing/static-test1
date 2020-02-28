![inmarsatLogo](https://www.inmarsat.com/wp-content/themes/inmarsat/img/icons/logo.png "Inmarsat Logo")

# Integration Release Plan (IRP)
## Purpose
The following documents the plan to release the service in this repository to production.
<br/>
**PLEASE NOTE:**
* The IRP must be completed prior to production release.
* The IRP should be kept up to date as often as possible to maintain accuracy and timeliness of the production release.
* **Please do not put sensitive information in the IRP (passwords, keys, etc.).** This information should be encrypted or provided separately to Business Applications Support.

## 1. Ready for Use
This section determines the usability of a service upon release to production. If a service is to be released without the intention to be used by end-users (for reasons such as it requires more testing), it is considered a _technical release_.

[comment]: <TYPE AN X BETWEEN THE BRACKETS OF ONE OF THE OPTIONS BELOW>
* Is this Service ready for release to production? YES[] NO[]
* Is this Service ready for use by end-users upon release (not a technical release)? YES[] NO[]

## 2. Deployment Instructions
This section outlines any steps required to prepare and deploy the service.
<br/>
**PLEASE NOTE:** The ESB repo name will outline the path to the Jenkins job.

1. Log in to Jenkins and click on the domain of the current repo (**ESB-Domain**-Subdomain-Function)
2. Within the domain folder of Jenkins, click on the subdomain and function of the service (ESB-Domain-**Subdomain-Function**)
3. Within the main build page for the service, click on the **OSDEPromote** job
4. Within the OSDE Promote job, click on the **Build with Parameters** button
5. Complete the following parameter fields and press Build:
* RECREATE_ARTIFACTS: Yes
* IMAGE_STREAM_TAG: (version specified in release branch (X.X.X))
* RELEASE_BRANCH_NAME: (release branch name)

## 3. Testing Instructions
Outline any steps required to ensure the service works as required.
1. Ensure the application starts with no errors in the logs
2. Verify with the developer(s) the service is working as required
3. Verify the project is relative to the project plan (if required)

## 4. Rollback Instructions
In the event of an unsuccessful release, document the instructions to back-out the release changes (if required).

1. Log in to Jenkins and click on the domain of the current repo (**ESB-Domain**-Subdomain-Function)
2. Within the domain folder of Jenkins, click on the subdomain and function of the service (ESB-Domain-**Subdomain-Function**)
3. Within the main build page for the service, click on the **OSDEPromote** job
4. Within the OSDE Promote job, click on the **Build with Parameters** button
5. Complete the following parameter fields and press Build:
* RECREATE_ARTIFACTS: Yes
* IMAGE_STREAM_TAG: (previous version tagged in GitHub (X.X.X))
* RELEASE_BRANCH_NAME: master

## 5. Service Impact
[comment]: <TYPE AN X BETWEEN THE BRACKETS OF ONE OF THE OPTIONS BELOW>
#### 1. Customer Impact
Does this service have impact on an Inmarsat customer? YES[] NO[]

If YES please provide customer name or customer group info;

-

#### 2. Business Impact
Does this service have impact on Inmarsat itself? YES[] NO[]

If YES please provide business impact;

- 

#### 3. Criticality
What is the level of criticality that this service has? LOW[] MEDIUM[] HIGH[] CRITICAL[]

If YES please provide business impact;

-

## 6. Dependent Services and Applications
Please list all services and applications (owned by Inmarsat or externally) that **are dependent on this service.**

[comment]: <ADD EACH SERVICE AS A LIST ITEM>

- 

Please list all services and applications (owned by Inmarsat or externally) that **this service depends on.**

[comment]: <ADD EACH SERVICE AS A LIST ITEM>

- 

## 7. Business Owners
Who are the business owners of this service? (Please provide names and contact information)

## 8. Outage Contacts
Who needs to be contacted in case there is an outage with this service? (Please provide names and contact information)

## 9. Other Support Contacts (Optional)
Are there any additional support contacts? (Please provide names and contact information)

<br/>
<br/>

###### Confidential and Proprietary - Inmarsat Global Limited
###### inmarsat.com
