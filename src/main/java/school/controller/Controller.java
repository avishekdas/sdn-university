/*
 * Copyright [2011-2016] "Neo Technology"
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 *
 */
package school.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import school.controller.exception.NotFoundException;
import school.domain.Entity;
import school.service.Service;

public abstract class Controller<T> {

    public abstract Service<T> getService();

    @RequestMapping(method = RequestMethod.GET)
    public Iterable<T> list(final HttpServletResponse response) {
        setHeaders(response);
        return list();
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public T create(@RequestBody T entity, final HttpServletResponse response) {
        setHeaders(response);
        return create(entity);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public T find(@PathVariable Long id, final HttpServletResponse response) {
        setHeaders(response);
        return find(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable Long id, final HttpServletResponse response) {
        setHeaders(response);
        delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json")
    public T update(@PathVariable Long id, @RequestBody T entity, final HttpServletResponse response) {
        setHeaders(response);
        return update(id, entity);
    }

    public void setHeaders(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate");
        response.setHeader("Expires", "0");
        response.setHeader("Pragma", "no-cache");
    }

    public Iterable<T> list() {
        return getService().findAll();
    }

    public  T create (T entity) {
        return getService().createOrUpdate(entity);
    }

    public T find(Long id) {
        T entity = getService().find(id);
        if (entity != null) {
            System.out.println("from OGM: " + entity);
            return entity;
        }
        throw new NotFoundException();
    }

    public void delete (Long id) {
        if (getService().find(id) != null) {
            getService().delete(id);
        } else {
            throw new NotFoundException();
        }
    }

    public  T update (Long id, T entity) {
        if (getService().find(id) != null) {
            return getService().createOrUpdate(entity);
        }
        throw new NotFoundException();
    }

}
