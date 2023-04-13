import type {Meta, StoryObj} from '@storybook/react';
import {CreateRepositoryForm} from "./CreateRepositoryPage";
import {RepositoryAccessMode} from "../../Repository";

const meta: Meta<typeof CreateRepositoryForm> = {
    title: "Create Repository Form",
    component: CreateRepositoryForm,
};

export default meta;
type Story = StoryObj<typeof CreateRepositoryForm>;

export const Default: Story = {
    args: {
        name: "Some title",
        accessMode: RepositoryAccessMode.PUBLIC,
        description: "Text",
        setName: (name: string) => {},
        setNameError: (error: string | undefined) => {},
        setAccessMode: (accessMode: RepositoryAccessMode) => {},
        setDescription: (description: string) => {},
    },
};
