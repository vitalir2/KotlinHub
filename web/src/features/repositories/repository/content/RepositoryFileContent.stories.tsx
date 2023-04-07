import type {Meta, StoryObj} from '@storybook/react';

import {RepositoryFileContent} from "./RepositoryFileContent";

const meta: Meta<typeof RepositoryFileContent> = {
    title: "Repository File Content",
    component: RepositoryFileContent,
};

export default meta;
type Story = StoryObj<typeof RepositoryFileContent>;

export const PlainText: Story = {
    render: () => <RepositoryFileContent fileName={"SomeText.txt"} fileContent={
        `Some text is written here
    And it's multilined too`}/>
};

export const Kotlin: Story = {
    render: () => <RepositoryFileContent fileName={"Hello.kt"} fileContent={`
    fun main() {
        println("Hello world!")
    }
    `}/>
};
