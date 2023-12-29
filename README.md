# DeskPro Android Messenger
![Messenger SDK Android CI](https://github.com/deskpro/messenger-sdk-android/workflows/Messenger%20SDK%20Android%20CI/badge.svg)

DeskPro Android Messenger is a Chat/AI/Messaging product. You can embed a “widget” directly into native app, so that enables end-users to use the product. Similar implementation for [iOS](https://github.com/deskpro/messenger-sdk-ios).

## Getting Started

You need an IDE with good Gradle and Kotlin integration to work with this project, which means either [Android Studio](https://developer.android.com/studio) or [IntelliJ](https://www.jetbrains.com/idea/download/) at the time of writing. Android Studio is the official IDE for Android development, and it receives regular updates and support from Google. It is designed specifically for Android development and includes tools and features tailored to the Android platform. If you already have IntelliJ and absolutely do not want to install another IDE, you can use that - but if you encounter any issues, don't waste your time and just get Android Studio.

From that point, you can just open the project in the IDE and you should be set to go.

For a general tutorial on creating Android library/SDK, [take a look here](https://developer.android.com/studio/projects/android-library).

## Running the tests

To execute all tests, run the following command: `./gradlew clean testDebugUnitTest --info --stacktrace`

## Deployment

This can be done in two ways:

1. Manually - Generate the .aar binary and then, it needs to be checked in with the respective native client repositories inside their directory.
2. Automatically - Create a new release on Github where it is necessary to specify 'Tag' and 'target branch'. After publishing, it will automatically appear on package repository - [Jitpack](https://jitpack.io/) platform.

After that, the library will be able to be installed as a dependency:

`dependencies {
	        implementation 'com.github.deskpro:messenger-sdk-android:Tag'
	}`

 Additional installation instructions are available on the platform itself.

### Branch Naming
A git branch should be named coherently in order to make sure the connection to Trello is given.
Generally most projects have 2 branches 'main' and 'develop', whereas 'main' is the code-base that is currently deployed to production and 'develop' is the somewhat stable development base for new features and the current code-base for the test system.
New features branch from the develop branch and should be named feat/[TrelloTicket]-a-good-description.
Bugfixes should be named bugfix/[TrelloTicket]-a-good-description or fix/[TrelloTicket]-a-good-description.

### Commit Guidelines
Commits should be written in english.
Commits should have the Trello Ticket number as prefix. e.g.:
DP-123 Add some awesome feature
How to write good commits is often hard but necessary. Generally there are 7 rules of great git commit messages:
* Separate subject from body with a blank line
* Limit the subject line to 50 characters
* Capitalize the subject line
* Do not end the subject line with a period
* Use the imperative mood in the subject line
* Wrap the body at 72 characters
* Use the body to explain what and why vs. how
Further readings and examples are in this [blogpost](https://chris.beams.io/posts/git-commit/)

### Pull Request Guidelines
A pull request should be opened, when the developer has finished developing his/her User Stories and thinks the acceptance criteria are met. Once opened, the developer should assign reviewers who review the code. Incorporate feasible feedback and try to resolve the occurring discussions.

#### Merge Guidelines
After enough approves for the Pull Request are gathered and all the automated checks pass the Pull Request can be merged.
GitHub offers 3 options on how to merge a Pull Request:
* Merge pull request
* Squash and merge
* Rebase and merge

The first option merge pull request will do a normal no fast forward merge into the target branch.
The second option will squash all your commits in the source branch in to one commit and then merge this one commit into the target branch.
The third option will replay all your commits in the source branch on top of the target branch. Errors can occur more easily here so this approach is not recommended.
 
We will leave it optional whether to merge via the first or second approach. However, developers should adhere to the conventional commit guidelines when merging a Pull Request. This makes it easier to automate semantic version bumps and changelog creation.
A conventional commit has this basic structure:

<type>[(optional scope)]: <description>

[optional body]

[optional footer] 


available types:

* feat -	when adding a new feature	minor (1.x.0)
* fix -	when fixing a bug	patch (1.1.x)
* perf - performance improvement without adding features or changing interfaces	patch (1.1.x)
* chore -	when doing some chores e.g. removing old references, doing a release
* refactor -	refactorings
* ci -	changes to the continuous integration
* docs -	adding documentation
* build -	when updating build scripts
* style -	improve code style (changing indents, bracket placements, ...)
 
If a breaking change is within the pull request please add BREAKING CHANGE: to the optional body and describe what and why it is one. This triggers a major version bump no matter which <type> is used.
 
For <description> and <optional body> our normal commit guidelines are still valid.

Examples:
Commit message with description and breaking change in body
feat: DP-123 Allow provided config object to extend other configs (#12)

BREAKING CHANGE: `extends` key in config file is now used for extending other config files 

Closes #12, DP-123
Commit message with scope
feat(lang): DP-143 Add german language (#21)

Closes #21, DP-143
Commit message for a fix
fix: DP-432 Fix minor typos in the DeskPro description (#41)

Fixes #41, DP-432


## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/deskpro/messenger-sdk-android/tags).

